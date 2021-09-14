package cn.byteboy.demo.jvm.nio.client;

import cn.byteboy.demo.jvm.nio.base.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author hongshaochuan
 * @date 2021/8/8
 */
public class NIOSocketClient extends SocketClient {

    private static final Logger LOG = LoggerFactory.getLogger(NIOSocketClient.class);

    private final Selector selector = Selector.open();

    private SocketChannel sock;

    private SelectionKey sockKey;

    // 用于读取数据包的长度
    protected final ByteBuffer lenBuffer = ByteBuffer.allocateDirect(4);

    // 先读数据包长度，之后调用 readLength()，然后再读完整的数据包
    protected ByteBuffer incomingBuffer = lenBuffer;

    private final LinkedBlockingQueue<Packet> outgoingQueue = new LinkedBlockingQueue<>();

    public NIOSocketClient() throws IOException {

    }

    @Override
    public void start() {
        new ReadThread(msg -> LOG.info("received msg: {}", msg)).start();
    }

    @Override
    public void connect(InetSocketAddress addr) throws IOException {
        SocketChannel sock = createSock();
        this.sock = sock;
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
        if (!connected) {
            LOG.debug("connect failed");
        }
    }

    void readLength() throws IOException {
        int len = incomingBuffer.getInt();
        incomingBuffer = ByteBuffer.allocate(len);
    }

    private class ReadThread extends Thread {

        private final MsgWatcher watcher;

        public ReadThread(MsgWatcher watcher) {
            this.watcher = watcher;
        }

        void readPacket(ByteBuffer incomingBuffer) {
            byte[] bytes = new byte[incomingBuffer.remaining()];
            incomingBuffer.get(bytes);
            Packet packet = new Packet(bytes);
            watcher.onMsg(packet);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    selector.select();

                    Set<SelectionKey> selected = selector.selectedKeys();
                    for (SelectionKey k : selected) {
                        SocketChannel sc = (SocketChannel) k.channel();
                        if ((k.readyOps() & SelectionKey.OP_CONNECT) != 0) {
                            if (sc.finishConnect()) {
                                LOG.debug("connect success");
                            }
                        }
                        if ((k.readyOps() & (SelectionKey.OP_READ | SelectionKey.OP_WRITE)) != 0) {
                            if (k.isReadable()) {
                                int rc = sc.read(incomingBuffer);
                                if (rc < 0) {
                                    // log error
                                }
                                // 判断当前buffer是否写满
                                if (!incomingBuffer.hasRemaining()) {
                                    incomingBuffer.flip();  // 切换为读模式
                                    if (incomingBuffer == lenBuffer) {
                                        readLength();
                                    } else {
                                        readPacket(incomingBuffer);
                                        lenBuffer.clear();
                                        incomingBuffer = lenBuffer;
                                    }
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class SendThread extends Thread {

        private final BlockingQueue<Packet> sendQueue;

        public SendThread() {
            this.sendQueue = new LinkedBlockingDeque<>();
            setDaemon(true);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Packet packet = sendQueue.take();
                    if (sock.isConnected()) {
                        ByteBuffer bb = packet.getBuffer();
                        while (bb.hasRemaining()) {
                            sock.write(bb);
                        }
                    }

                } catch (Exception e) {

                }

            }
        }
    }

    private interface MsgWatcher {
        void onMsg(Packet msg);
    }
}
