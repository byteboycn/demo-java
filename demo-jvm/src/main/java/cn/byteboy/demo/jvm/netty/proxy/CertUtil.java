package cn.byteboy.demo.jvm.netty.proxy;

import io.netty.handler.ssl.util.SelfSignedCertificate;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author hongshaochuan
 */
public class CertUtil {

    private static SelfSignedCertificate ssc;


    /**
     * 生成RSA公私密钥对,长度为2048
     */
    public static KeyPair genKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048, new SecureRandom());
        return keyPairGenerator.genKeyPair();
    }

    public static X509Certificate getCert() {
        return null;
    }

    public static SelfSignedCertificate getSsc() {
        synchronized (CertUtil.class) {
            if (ssc == null) {
                try {
                    ssc = new SelfSignedCertificate();
                } catch (CertificateException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return ssc;
    }
}
