package cn.byteboy.demo.socket.server;

/**
 * @author hongshaochuan
 */
public class Server {

    private static Server server;

    public static Server getServer() {
        return server;
    }

    public static void start(String[] args) {
        server = new Server();

    }
}
