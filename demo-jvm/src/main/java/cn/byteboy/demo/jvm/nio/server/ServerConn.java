package cn.byteboy.demo.jvm.nio.server;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author hongshaochuan
 * @date 2021/8/7
 *
 * a connection from a client to the server
 */
public class ServerConn {

    private final SocketChannel sock;

    private final SelectionKey sk;

    private final AtomicBoolean selectable = new AtomicBoolean(true);

    private final Queue<ByteBuffer> outgoingQueue = new LinkedBlockingQueue<>();

    public boolean isSelectable() {
        return sk.isValid() && selectable.get();
    }

    public void disableSelectable() {
        selectable.set(false);
    }

    public void enableSelectable() {
        selectable.set(true);
    }

    public ServerConn(SocketChannel sock, SelectionKey sk) {
        this.sock = sock;
        this.sk = sk;
    }

    public int getInterestOps() {
        if (!isSelectable()) {
            return 0;
        }
        int interestOps = 0;
        interestOps |= SelectionKey.OP_READ;

        if (!outgoingQueue.isEmpty()) {
            interestOps |= SelectionKey.OP_WRITE;
        }
        return interestOps;
    }

    void doIO(SelectionKey k) {

    }
}
