package cn.byteboy.demo.jvm.netty.proxy;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;


/**
 * @author hongshaochuan
 */
@Slf4j
public class ProxyServer {

    //http代理隧道握手成功
    public final static HttpResponseStatus SUCCESS = new HttpResponseStatus(200,
            "Connection established");


    public void start(final int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_KEEPALIVE, false)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline()
                                    .addLast("httpCodec", new HttpServerCodec())
                                    .addLast("serverHandler", new ServerHandler());

                        }
                    });
            Channel ch = b.bind(port).sync().channel();
            log.info("proxy server start with {}", port);
//            ch.closeFuture()
        } catch (Exception e) {
            e.printStackTrace();
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static class ServerHandler extends ChannelInboundHandlerAdapter {

        enum Stage { ONE, TWO, THREE }

        private ProtoUtil.RequestProto requestProto;

        private Stage stage = Stage.ONE;

        private ChannelFuture ccf;

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg instanceof HttpRequest) {
                HttpRequest req = (HttpRequest) msg;

                if (stage == Stage.ONE) {
                    stage = Stage.TWO;
                    // 非首次链接时回导致解析的信息不准确
                    requestProto = ProtoUtil.getRequestProto((HttpRequest) msg);
                    // 代理握手 (ssl)
                    if (HttpMethod.CONNECT.name().equals(req.method().name())) {
                        HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, SUCCESS);
                        ctx.writeAndFlush(response);
                        ctx.pipeline().remove("httpCodec");
                        ReferenceCountUtil.release(msg);
                        return;
                    }
                }


                handleProxyData(ctx.channel(), req, true);
            } else if (msg instanceof HttpContent) {
                ReferenceCountUtil.release(msg);
            } else {
                ByteBuf byteBuf = (ByteBuf) msg;
                // ssl握手
                if (ProxyConfig.isHandleSSL && byteBuf.getByte(0) == 22) {
                    requestProto.setSsl(true);
                    SelfSignedCertificate ssc = CertUtil.getSsc();
                    SslContext sslContext = SslContextBuilder
                            .forServer(ssc.certificate(), ssc.privateKey())
                            .build();
                    ctx.pipeline().addFirst("httpCodec", new HttpServerCodec());
                    ctx.pipeline().addFirst("sslHandle", sslContext.newHandler(ctx.alloc()));
                    ctx.pipeline().fireChannelRead(msg);
                    return;
                }

                handleProxyData(ctx.channel(), msg, false);
            }
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            super.exceptionCaught(ctx, cause);
        }

        private void handleProxyData(Channel channel, Object msg, boolean isHttp) {

            if (ccf == null) {
                Bootstrap b = new Bootstrap();
                b.group(ProxyConfig.eventLoopGroup)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.SO_KEEPALIVE, false)
                        .handler(new ChannelInitializer<Channel>() {
                            @Override
                            protected void initChannel(Channel ch) throws Exception {
                                if (isHttp) {
                                    if (requestProto.isSsl()) {
                                        ch.pipeline().addLast(ProxyConfig.cSslCtx.newHandler(ch.alloc(), requestProto.getHost(), requestProto.getPort()));
                                    }
                                    ch.pipeline().addLast("httpCodec", new HttpClientCodec());
                                    ch.pipeline().addLast("aggregator", new HttpObjectAggregator( 1024 * 1024 * 8));
                                }
                                ch.pipeline().addLast("proxyClientHandle", new HttpProxyClientHandler(channel));
                            }
                        });
                ccf = b.connect(requestProto.getHost(), requestProto.getPort());
                ccf.addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        future.channel().writeAndFlush(msg);
                    }
                });
                ChannelFuture closeFuture = ccf.channel().closeFuture();
                closeFuture.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        System.out.println("操作完成");
                    }
                });
            } else {
                ccf.channel().writeAndFlush(msg);
            }

        }
    }

    public static class HttpProxyClientHandler extends ChannelInboundHandlerAdapter {

        private final Channel sChannel;

        public HttpProxyClientHandler(Channel sChannel) {
            this.sChannel = sChannel;
        }
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg instanceof FullHttpResponse) {
                FullHttpResponse fullHttpResponse = (FullHttpResponse) msg;
                ByteBuf content = fullHttpResponse.content();
            }
            if (msg instanceof ByteBuf) {
                ByteBuf bb = (ByteBuf) msg;
                System.out.println(bb.readableBytes());
            }
            sChannel.writeAndFlush(msg);
        }

        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
            System.out.println("channel 关闭");
            super.channelUnregistered(ctx);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("channel channelInactive");
            super.channelInactive(ctx);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            super.exceptionCaught(ctx, cause);
        }
    }


}
