package cn.byteboy.demo.jvm.sshd.sftp;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.sftp.client.SftpClient;
import org.apache.sshd.sftp.client.SftpClientFactory;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author hongshaochuan
 * @Date 2021/9/26
 */
public class SftpFactory {

    SftpClient create(String user, String host, int port, String password) {
//        SshClient client = SshClient.setUpDefaultClient();
//        // do configuration the client
//        client.start();
//        try (ClientSession session = client.connect(user, host, port).verify().getSession()) {
//
//            session.addPasswordIdentity(password); // for password
//            // session.addPublicKeyIdentity(); // KeyPair
//
//            if (session.auth().verify().isFailure())
//                throw new RuntimeException("连接失败");
//
//            SftpClientFactory factory = SftpClientFactory.instance();
//            try (SftpClient sftp = factory.createSftpClient(session)) {
//                try (OutputStream os = sftp.write("/sftp/11.txt", SftpClient.OpenMode.Write)) {
//                    os.write("test for sftp".getBytes(StandardCharsets.UTF_8));
//                }
//            }
        return null;
    }
}
