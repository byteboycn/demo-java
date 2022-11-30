package cn.byteboy.demo.socket.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import static io.netty.channel.ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE;

/**
 * @author hongshaochuan
 */
public class NettyAcceptor {

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private Class<? extends ServerSocketChannel> channelClass;

    private int soBacklog;

    private boolean soReuseaddr;

    private boolean tcpNoDelay;

    private boolean soKeepalive;

    private abstract static class PipelineInitializer {
        abstract void init(SocketChannel channel) throws Exception;
    }

    public void initialize() {

        boolean epoll = true;
        soBacklog = 1024;
        soReuseaddr = true;
        tcpNoDelay = true;
        soKeepalive = true;


        if (epoll) {
            bossGroup = new EpollEventLoopGroup();
            workerGroup = new EpollEventLoopGroup();
            channelClass = EpollServerSocketChannel.class;
        } else {
            bossGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();
            channelClass = NioServerSocketChannel.class;
        }
    }

    private void initializeWebsocketTransport() {

        String host = "";
        int port = 8888;
        String path = "";
        initFactory(host, port, "Websocket", new PipelineInitializer() {
            @Override
            void init(SocketChannel channel) throws Exception {
                ChannelPipeline pipeline = channel.pipeline();

            }
        });


    }

    private void initFactory(String host, int port, String protocol, PipelineInitializer pipelineInitializer) {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(channelClass)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        pipelineInitializer.init(channel);
                    }
                })
                .option(ChannelOption.SO_BACKLOG, soBacklog)
                .option(ChannelOption.SO_REUSEADDR, soReuseaddr)
                .childOption(ChannelOption.TCP_NODELAY, tcpNoDelay)
                .childOption(ChannelOption.SO_KEEPALIVE, soKeepalive);

        try {
            ChannelFuture f = b.bind(host, port);
            f.sync().addListener(FIRE_EXCEPTION_ON_FAILURE);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }


    }
}
