package cn.byteboy.demo.jvm.netty.proxy;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.CLOSE;
import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_PLAIN;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

/**
 * @author hongshaochuan
 */
public class ProxyServer {


    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline()
                                    .addLast("httpCodec", new HttpServerCodec())
                                    .addLast("serverHandler", new ServerHandler());

                        }
                    });
            Channel ch = b.bind(9000).sync().channel();
            ch.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static class ServerHandler extends ChannelInboundHandlerAdapter {

        private static final byte[] CONTENT = { 'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd' };
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println(msg);
            if (msg instanceof HttpRequest) {
                HttpRequest req = (HttpRequest) msg;
                // basic http response
//                FullHttpResponse response = new DefaultFullHttpResponse(req.protocolVersion(), OK,
//                        Unpooled.wrappedBuffer(CONTENT));
//                response.headers()
//                        .set(CONTENT_TYPE, TEXT_PLAIN)
//                        .setInt(CONTENT_LENGTH, response.content().readableBytes());
//                response.headers().set(CONNECTION, CLOSE);
//                ChannelFuture f = ctx.write(response);
//                f.addListener(ChannelFutureListener.CLOSE);

                handleProxyData(ctx.channel(), req);
            }
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
        }

        private void handleProxyData(Channel channel, Object msg) {

            if (msg instanceof HttpRequest) {
                ProtoUtil.RequestProto requestProto = ProtoUtil.getRequestProto((HttpRequest) msg);
                Bootstrap b = new Bootstrap();
                b.group(new NioEventLoopGroup())
                        .channel(NioSocketChannel.class)
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
    }


}
