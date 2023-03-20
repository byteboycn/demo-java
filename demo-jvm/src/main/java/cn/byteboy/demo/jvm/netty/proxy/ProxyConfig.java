package cn.byteboy.demo.jvm.netty.proxy;

import java.security.KeyPair;

/**
 * @author hongshaochuan
 */
public class ProxyConfig {


    public final static KeyPair keyPair;

    static {
        try {
            keyPair = CertUtil.genKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
