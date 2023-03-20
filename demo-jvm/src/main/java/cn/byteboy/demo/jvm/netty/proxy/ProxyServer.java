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
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;


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
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg instanceof HttpRequest) {
                HttpRequest req = (HttpRequest) msg;

                if (HttpMethod.CONNECT.name().equals(req.method().name())) {
                    HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, SUCCESS);
                    ctx.writeAndFlush(response);
                    ctx.channel().pipeline().remove("httpCodec");
                    ReferenceCountUtil.release(msg);
                    return;
                }
//                handleProxyData(ctx.channel(), req, true);
            } else if (msg instanceof HttpContent) {
                ReferenceCountUtil.release(msg);
            } else {
                ByteBuf byteBuf = (ByteBuf) msg;
                if (byteBuf.getByte(0) == 22) {
//                    SslContextBuilder.forServer()
                }
                System.out.println(byteBuf);
            }
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
        }

        private void handleProxyData(Channel channel, Object msg, boolean isHttp) {

            if (msg instanceof HttpRequest) {
                ProtoUtil.RequestProto requestProto = ProtoUtil.getRequestProto((HttpRequest) msg);
                Bootstrap b = new Bootstrap();
                b.group(new NioEventLoopGroup())
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.SO_KEEPALIVE, false)
                        .handler(new ChannelInitializer<Channel>() {
                            @Override
                            protected void initChannel(Channel ch) throws Exception {
                                ch.pipeline()
                                        .addLast("httpCodec", new HttpClientCodec())
                                        .addLast("proxyClientHandle", new HttpProxyClientHandler(channel));
                            }
                        });
                ChannelFuture cf = b.connect(requestProto.getHost(), requestProto.getPort());
                cf.addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        future.channel().writeAndFlush(msg);
                    }
                });
                ChannelFuture closeFuture = cf.channel().closeFuture();
                closeFuture.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        System.out.println("操作完成");
                    }
                });

            } else if (msg instanceof HttpContent) {

            } else {

            }

        }
    }

    public static class HttpProxyClientHandler extends ChannelInboundHandlerAdapter {

        private final Channel clientChannel;

        public HttpProxyClientHandler(Channel clientChannel) {
            this.clientChannel = clientChannel;
        }
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            clientChannel.writeAndFlush(msg);
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
    }


}
