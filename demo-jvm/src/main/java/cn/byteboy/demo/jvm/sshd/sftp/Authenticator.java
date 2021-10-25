package cn.byteboy.demo.jvm.sshd.sftp;

/**
 * @author hongshaochuan
 * @Date 2021/9/26
 */
public class Authenticator {
    enum Type {
        PASSWORD, PUBLIC_KEY
    }

    Authenticator createWithPassword(String password) {
        return null;
    }

    Authenticator createWithPublicKey(String key) {
        return null;
    }
}
