package cn.byteboy.demo.jvm.nio.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * @author hongshaochuan
 * @date 2021/8/8
 */
public class NIOSocketClient extends SocketClient {

    private static final Logger LOG = LoggerFactory.getLogger(NIOSocketClient.class);

    private final Selector selector = Selector.open();

    private SelectionKey sockKey;

    public NIOSocketClient() throws IOException {

    }

    @Override
    void start() {

    }

    @Override
    void connect(InetSocketAddress addr) throws IOException {
        SocketChannel sock = createSock();
        try {
            registerAndConnect(sock, addr);
        } catch (Exception e) {
            LOG.error("Unable to open socket to {}", addr);
            sock.close();
            throw e;
        }
    }

    SocketChannel createSock() throws IOException {
        SocketChannel sock = SocketChannel.open();
        sock.configureBlocking(false);
        sock.socket().setSoLinger(false, -1);
        sock.socket().setTcpNoDelay(true);
        return sock;
    }

    void registerAndConnect(SocketChannel sock, InetSocketAddress addr) throws IOException {
        sockKey = sock.register(selector, SelectionKey.OP_CONNECT);
        boolean connected = sock.connect(addr);
    }

    class SendThread extends Thread {

        public SendThread() {
            setDaemon(true);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    selector.select();

                    Set<SelectionKey> selected = selector.selectedKeys();
                    for (SelectionKey k : selected) {
                        if ((k.readyOps() & (SelectionKey.OP_READ | SelectionKey.OP_WRITE)) != 0) {

                        }
                    }
                } catch (IOException e) {

                }

            }
        }
    }
}
