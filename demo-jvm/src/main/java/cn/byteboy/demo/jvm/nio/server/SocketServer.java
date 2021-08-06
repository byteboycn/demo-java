package cn.byteboy.demo.jvm.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author hongshaochuan
 * @Date 2021/8/6
 */
public class SocketServer {

    // boss group
    private AcceptThread acceptThread;

    // worker group
    private final Set<SelectorThread> selectorThreads = new HashSet<>();

    // accept the connections within acceptThread
    private ServerSocketChannel ss;

    private int numSelectorThreads;

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
        acceptThread = new AcceptThread();
    }



    private abstract class AbstractSelectThread extends Thread {

        protected final Selector selector;

        public AbstractSelectThread(String name) throws IOException {
            this.selector = Selector.open();
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

        public AcceptThread() throws IOException {
            super("AcceptThread");
            this.acceptChannel = ServerSocketChannel.open();
            this.acceptChannel.socket().setReuseAddress(true);
            this.acceptChannel.configureBlocking(false);
            this.acceptChannel.register(selector, SelectionKey.OP_ACCEPT);
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

            } catch (IOException e) {
                e.printStackTrace();
            }

            return accepted;
        }
    }

    private class SelectorThread extends AbstractSelectThread {

        public SelectorThread(int id) throws IOException {
            super("SelectorThread-" + id);
        }

        @Override
        public void run() {
            while (true) {
                select();
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

        }
    }


}
