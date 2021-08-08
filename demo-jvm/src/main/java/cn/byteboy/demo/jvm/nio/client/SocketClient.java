package cn.byteboy.demo.jvm.nio.client;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author hongshaochuan
 * @date 2021/8/8
 */
public abstract class SocketClient {

//    protected ClientConn.SendThread sendThread;

    abstract void connect(InetSocketAddress addr) throws IOException;

    abstract void start();
}
