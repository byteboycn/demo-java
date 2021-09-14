package cn.byteboy.demo.jvm.nio.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author hongshaochuan
 * @Date 2021/8/6
 *
 * 维护 acceptThread 和 selectorThreads
 * acceptThread 用于接受新的连接，并且分发给 selectorThreads，每个真实的连接对应一个 ServerConn
 */
public class NIOSocketServer extends SocketServer {

    private static final Logger LOG = LoggerFactory.getLogger(NIOSocketServer.class);

    // boss group
    private AcceptThread acceptThread;

    // worker group
    private final Set<SelectorThread> selectorThreads = new HashSet<>();

    // accept the connections within acceptThread
    private ServerSocketChannel ss;

    private int numSelectorThreads;

    private volatile boolean stopped = true;

    protected WorkerService workerService;

    private int numWorkerThreads;


    @Override
    public void configure(InetSocketAddress addr) throws IOException {

        int numCores = Runtime.getRuntime().availableProcessors();
        numSelectorThreads = Math.max((int) Math.sqrt((float) numCores / 2), 1);

        for (int i = 0; i < numSelectorThreads; i++) {
            selectorThreads.add(new SelectorThread(i));
        }

        // init ss
        this.ss = ServerSocketChannel.open();
        this.ss.socket().setReuseAddress(true);
        this.ss.configureBlocking(false);
        acceptThread = new AcceptThread(ss, selectorThreads);

        numWorkerThreads = 2 * numCores;

        String logMsg = "Configuration NIO connection handler with "
                + numSelectorThreads + " selector thread(s), "
                + numWorkerThreads + " worker threads.";
        LOG.info(logMsg);
        LOG.info("binding to port {}", addr);
        this.ss.socket().bind(addr);
    }

    @Override
    public void start() {
        stopped = false;
        if (workerService == null) {
            workerService = new WorkerService("NIOWorker", numWorkerThreads);
        }

        for (SelectorThread t : selectorThreads) {
            if (t.getState() == Thread.State.NEW) {
                t.start();
            }
        }

        if (acceptThread.getState() == Thread.State.NEW) {
            acceptThread.start();
        }

    }



    private abstract class AbstractSelectThread extends Thread {

        protected final Selector selector;

        public AbstractSelectThread(String name) throws IOException {
            this.selector = Selector.open();
        }

        public void wakeupSelector() {
            selector.wakeup();
        }

        protected void closeSelector() {
            try {
                selector.close();
            } catch (IOException e) {

            }
        }
    }

    private class AcceptThread extends AbstractSelectThread {

        private final ServerSocketChannel acceptChannel;

        private final Collection<SelectorThread> selectorThreads;

        private Iterator<SelectorThread> selectorIterator;

        public AcceptThread(ServerSocketChannel ss, Set<SelectorThread> selectorThreads) throws IOException {
            super("AcceptThread");
            this.acceptChannel = ss;
            this.acceptChannel.register(selector, SelectionKey.OP_ACCEPT);
            this.selectorThreads = Collections.unmodifiableCollection(new ArrayList<>(selectorThreads));
            this.selectorIterator = selectorThreads.iterator();
        }

        @Override
        public void run() {
            try {
                while (!acceptChannel.socket().isClosed()) {
                    select();
                }
            } finally {
                closeSelector();
            }

        }

        private void select() {
            try {
                /**
                 * 阻塞住当前线程，当有以下两种情况时返回
                 * 1.至少有一个channel就绪，返回当前就绪的channel数量，这里的channel指当前selector敢兴趣的channel
                 * 2. wakeup method is invoked or the current thread is interrupted
                 */
                int readyChannelNum = selector.select();

                // readyChannelNum不为0时，会返回当前这些channel的SelectionKey
                Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
                while (selectedKeys.hasNext()) {
                    SelectionKey key = selectedKeys.next();

                    if (!key.isValid()) {
                        continue;
                    }
                    if (key.isAcceptable()) {
                        doAccept();
                    }

                }

            } catch (IOException e) {

            }
        }

        private boolean doAccept() {
            boolean accepted = false;
            SocketChannel sc = null;
            try {
                sc = acceptChannel.accept();
                accepted = true;
                sc.configureBlocking(false);

                LOG.debug("Accepted socket connection from {}", sc.socket().getRemoteSocketAddress());

                if (!selectorIterator.hasNext()) {
                    selectorIterator = selectorThreads.iterator();
                }
                SelectorThread selectorThread = selectorIterator.next();
                if (!selectorThread.addAcceptedConnection(sc)) {
                    throw new IOException("Unable to add connection to selector queue");
                }

            } catch (IOException e) {
                LOG.error("Error accepting new connection", e);
            }

            return accepted;
        }
    }

    private class SelectorThread extends AbstractSelectThread {

        private final Queue<SocketChannel> acceptedQueue;

        public SelectorThread(int id) throws IOException {
            super("SelectorThread-" + id);
            this.acceptedQueue = new LinkedBlockingDeque<>();
        }

        public boolean addAcceptedConnection(SocketChannel accepted) {
            if (!acceptedQueue.offer(accepted)) {
                return false;
            }
            wakeupSelector();
            return true;
        }

        @Override
        public void run() {
            while (true) {
                select();
                processAcceptedConnections();
            }
        }

        private void select() {
            try {
                selector.select();

                Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
                while (selectedKeys.hasNext()) {
                    SelectionKey key = selectedKeys.next();
                    if (!key.isValid()) {
                        cleanup(key);
                        continue;
                    }
                    if (key.isReadable() || key.isWritable()) {
                        handleIO(key);
                    } else {
                        // log something
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        protected void cleanup(SelectionKey key) {
            if (key != null) {
                try {
                    key.cancel();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void handleIO(SelectionKey key) {
            IOWorkRequest workRequest = new IOWorkRequest(this, key);
            workerService.schedule(workRequest);

        }

        private void processAcceptedConnections() {
            SocketChannel accepted;
            while ((accepted = acceptedQueue.poll()) != null) {
                SelectionKey key = null;
                try {
                    key = accepted.register(selector, SelectionKey.OP_READ);
                    ServerConn conn = createConnection(accepted, key);
                    key.attach(conn);
                } catch (IOException e) {

                }

            }
        }
    }


    protected ServerConn createConnection(SocketChannel sock, SelectionKey sk) {
        return new ServerConn(sock, sk);
    }

    private class IOWorkRequest extends WorkerService.WorkRequest {

        private final SelectorThread selectorThread;

        private final SelectionKey key;

        private final ServerConn conn;

        public IOWorkRequest(SelectorThread selectorThread, SelectionKey key) {
            this.selectorThread = selectorThread;
            this.key = key;
            this.conn = (ServerConn) key.attachment();
        }

        @Override
        public void doWork() throws Exception {
            if (!key.isValid()) {
                return;
            }
            if (key.isReadable() || key.isWritable()) {
                conn.doIO(key);
            }
        }
    }


}
