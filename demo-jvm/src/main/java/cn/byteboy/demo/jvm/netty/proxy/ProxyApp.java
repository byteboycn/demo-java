package cn.byteboy.demo.jvm.netty.proxy;

/**
 * @author hongshaochuan
 */
public class ProxyApp {

    // curl -k -x 127.0.0.1:9000 http://local:9000/
    public static void main(String[] args) {
        new ProxyServer().start();
    }

    public void startBasicHttpService() {
    }
}
