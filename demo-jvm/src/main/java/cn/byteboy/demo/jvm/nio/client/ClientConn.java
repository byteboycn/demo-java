//package cn.byteboy.demo.jvm.nio.client;
//
///**
// * @author hongshaochuan
// * @date 2021/8/8
// */
//public class ClientConn {
//
//    private final SendThread sendThread;
//
//    public ClientConn(SocketClient socketClient) {
//        this.sendThread = new SendThread(socketClient);
//    }
//
//    public void start() {
//        sendThread.start();
//    }
//
//    class SendThread extends Thread {
//
//        private final SocketClient socketClient;
//
//        public SendThread(SocketClient socketClient) {
//            this.socketClient = socketClient;
//            setDaemon(true);
//        }
//
//        @Override
//        public void run() {
//            while (true) {
//
//            }
//        }
//    }
//}
