package cn.byteboy.demo.jvm.nio.client;

import cn.byteboy.demo.jvm.nio.base.Packet;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author hongshaochuan
 * @date 2021/8/8
 */
public abstract class SocketClient {

//    protected ClientConn.SendThread sendThread;

    public abstract void connect(InetSocketAddress addr) throws IOException;

    public abstract void start();

    public abstract void send(Packet packet);
}
