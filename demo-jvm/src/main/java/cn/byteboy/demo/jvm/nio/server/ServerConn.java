package cn.byteboy.demo.jvm.nio.server;

import cn.byteboy.demo.jvm.nio.base.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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

    protected final ByteBuffer lenBuffer = ByteBuffer.allocateDirect(4);

    protected ByteBuffer incomingBuffer = lenBuffer;

    private static final Logger LOG = LoggerFactory.getLogger(ServerConn.class);

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

    // 重新分配incomingBuffer，使它的长度为数据包的长度
    private void readLength() {
        int len = incomingBuffer.getInt();
        // 这里暂时不考虑内存是否够用
        incomingBuffer = ByteBuffer.allocate(len);
    }

    private void readPayload() {

        if (incomingBuffer.remaining() == 0) {
            incomingBuffer.flip();
            // 这里切换读模式后，remaining是可读字节数
            byte[] bytes = new byte[incomingBuffer.remaining()];
            incomingBuffer.get(bytes);
            Packet packet = new Packet(bytes);
            LOG.info("received packet, len:{}, content:{}", bytes.length, packet.getMsg());

        }
    }

    void doIO(SelectionKey k) {
        try {
            if (!sock.isOpen()) {
                return;
            }
            if (k.isReadable()) {
                int rc = sock.read(incomingBuffer);
                if (rc < 0) {
                    // do something
                }
                // 如果已写满
                if (incomingBuffer.remaining() == 0) {
                    if (incomingBuffer == lenBuffer) {
                        incomingBuffer.flip();  // 切换为读模式
                        readLength();
                    } else {
                        readPayload();
                        lenBuffer.clear();
                        incomingBuffer = lenBuffer;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
