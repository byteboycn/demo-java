package cn.byteboy.demo.jvm.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author hongshaochuan
 * @date 2021/8/8
 */
public abstract class SocketServer {

    public abstract void configure(InetSocketAddress addr) throws IOException;

    public abstract void start();
}
