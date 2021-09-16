package cn.byteboy.demo.jvm.nio;

import cn.byteboy.demo.jvm.nio.base.Packet;
import cn.byteboy.demo.jvm.nio.client.NIOSocketClient;
import cn.byteboy.demo.jvm.nio.client.SocketClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * @author hongshaochuan
 * @date 2021/9/14
 */
public class DemoClient {

    public static void main(String[] args) throws IOException {
        SocketClient client = new NIOSocketClient();
        client.connect(new InetSocketAddress( InetAddress.getLocalHost(), 11206));
        client.start();

        // 发送数据包
        client.send(new Packet("ab"));
        client.send(new Packet("测试数据包"));
        client.send(new Packet("fuck"));
    }
}
