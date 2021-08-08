package cn.byteboy.demo.jvm.nio.server;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * @author hongshaochuan
 * @date 2021/8/7
 *
 * a connection from a client to the server
 */
public class ServerConn {

    private final SocketChannel sock;

    private final SelectionKey sk;

    public ServerConn(SocketChannel sock, SelectionKey sk) {
        this.sock = sock;
        this.sk = sk;
    }

    void doIO(SelectionKey k) {

    }
}
