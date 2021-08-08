package cn.byteboy.demo.jvm.nio.server;

import java.net.InetSocketAddress;

/**
 * @author hongshaochuan
 * @date 2021/8/8
 */
public class SocketServerFactory {

    public static SocketServer createServer(int port) throws Exception {
        String serverName = NIOSocketServer.class.getName();
        try {
            SocketServer socketServer = (SocketServer) Class.forName(serverName).getDeclaredConstructor().newInstance();
            socketServer.configure(new InetSocketAddress(port));
            return socketServer;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
