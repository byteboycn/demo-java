package cn.byteboy.demo.jvm.netty.proxy;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import javax.net.ssl.SSLException;
import java.security.KeyPair;

/**
 * @author hongshaochuan
 */
public class ProxyConfig {


    public final static KeyPair keyPair;

    public final static NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup(2);

    public final static boolean isHandleSSL = false;

    public final static SslContext cSslCtx;

    static {
        try {
            keyPair = CertUtil.genKeyPair();
            cSslCtx = SslContextBuilder
                    .forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
