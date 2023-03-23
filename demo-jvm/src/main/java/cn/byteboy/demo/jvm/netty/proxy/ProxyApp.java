package cn.byteboy.demo.jvm.netty.proxy;

import cn.hutool.core.date.DateUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.CLOSE;
import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_PLAIN;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

/**
 * @author hongshaochuan
 */
@Slf4j
public class ProxyApp {

    // curl -k -x 127.0.0.1:9000 http://local:9001/
    // curl -k -x 127.0.0.1:9000 https://www.baidu.com/
    // curl -k -x 127.0.0.1:9999 https://www.baidu.com/

//    networksetup -setwebproxy "Wi-Fi" 127.0.0.1 9000
//    networksetup -setsecurewebproxy "Wi-Fi" 127.0.0.1 9000
    public static void main(String[] args) {
        new ProxyServer().start(9000);
        new ProxyApp().startBasicHttpService(9001);

//        new ProxyApp().httpClient();
    }

    public void startBasicHttpService(int port) {
        final byte[] CONTENT = { 'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd' };
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(1);
        ServerBootstrap b = new ServerBootstrap();
        b.group(eventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline()
                                .addLast("httpCodec", new HttpServerCodec())
                                .addLast("basicHttpService", new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        if (msg instanceof HttpRequest) {
                                            HttpRequest req = (HttpRequest) msg;
                                            // basic http response
                                            FullHttpResponse response = new DefaultFullHttpResponse(req.protocolVersion(), OK, Unpooled.wrappedBuffer(CONTENT));
                                            response.headers().set(CONTENT_TYPE, TEXT_PLAIN).setInt(CONTENT_LENGTH, response.content().readableBytes());
                                            response.headers().set(CONNECTION, CLOSE);
                                            ctx.writeAndFlush(response);
                                        }
                                    }
                                });
                    }
                });


        try {
            Channel channel = b.bind(port).sync().channel();
            log.info("basic http service start with {}", port);
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void httpClient() {
        Bootstrap b = new Bootstrap();
        b.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class).handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast("httpCodec", new HttpClientCodec());
                        ch.pipeline().addLast("aggregator", new HttpObjectAggregator(1024 * 100));
                        ch.pipeline().addLast("clientHandler", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(msg);
                            }
                        });
                    }
                });
        try {
            FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/");
            ChannelFuture cf = b.connect("www.baidu.com", 443).sync();
            cf.channel().writeAndFlush(request);
            cf.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
