package cn.byteboy.demo.jvm.nio;

import cn.byteboy.demo.jvm.nio.server.SocketServer;
import cn.byteboy.demo.jvm.nio.server.SocketServerFactory;

/**
 * @author hongshaochuan
 * @date 2021/8/8
 */
public class DemoServer {

    public static void main(String[] args) throws Exception {
        SocketServer server = SocketServerFactory.createServer(11206);
        server.start();
    }
}
