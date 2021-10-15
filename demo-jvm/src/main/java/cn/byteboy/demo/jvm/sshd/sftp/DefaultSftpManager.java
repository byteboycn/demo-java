package cn.byteboy.demo.jvm.sshd.sftp;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.sftp.client.SftpClient;
import org.apache.sshd.sftp.client.SftpClientFactory;
import org.apache.sshd.sftp.common.SftpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.apache.sshd.sftp.common.SftpConstants.SSH_FX_FAILURE;
import static org.apache.sshd.sftp.common.SftpConstants.SSH_FX_NO_SUCH_FILE;

/**
 * @author hongshaochuan
 * @Date 2021/9/26
 */
public class DefaultSftpManager implements SftpManager {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultSftpManager.class);

    private final String username;

    private final String host;

    private final int port;

    private final String password;

    private SshClient client;

    private ClientSession session;

    private SftpClient sftpClient;

    private static final Duration timeout = Duration.ofMillis(3000);


    private volatile boolean connected = false;

    public DefaultSftpManager(String username, String host, int port, String password) {
        this.username = username;
        this.host = host;
        this.port = port;
        this.password = password;
    }

    public void connect() throws IOException {
        if (connected)
            return;

        client = SshClient.setUpDefaultClient();
        // do configuration the client
        client.start();

        session = client.connect(username, host, port).verify(timeout).getSession();
        System.out.println(session);

        session.addPasswordIdentity(password);
        // session.addPublicKeyIdentity(); // KeyPair

        if (!session.auth().verify(timeout).isSuccess())
            throw new IOException("连接失败");

        SftpClientFactory factory = SftpClientFactory.instance();
        sftpClient = factory.createSftpClient(session);

        connected = true;
    }


    public SftpClient getClient() throws IOException {
        if (!connected)
            connect();
        return sftpClient;
    }

    @Override
    public void close() throws IOException {
        if (!connected)
            return;
        client.stop();

        if (sftpClient != null) {
            sftpClient.close();
        }
        if (session != null) {
            session.close();
        }
    }

    public void checkConnected() {
        if (!connected) {
            throw new RuntimeException("run connect before");
        }
    }

    @Override
    public long upload(Path src, String destPath) throws IOException {
        checkConnected();
        try (OutputStream os = sftpClient.write(destPath, EnumSet.of(SftpClient.OpenMode.Write, SftpClient.OpenMode.Create, SftpClient.OpenMode.Truncate))) {
            return Files.copy(src, os);
        } catch (SftpException e) {
            if (e.getStatus() == SSH_FX_NO_SUCH_FILE) {
                LOG.error("文件夹不存在，请先创建文件夹。destPath = {}", destPath);
            }
            throw e;
        }
    }

    @Override
    public long uploadAndMkdirIfNecessary(Path src, String destPath) throws IOException {
        checkConnected();

        if (!destPath.startsWith("/"))
            throw new IOException("目标路径必须为绝对路径");

        List<String> paths = new ArrayList<>();
        String temp = destPath;
        while (!"".equals(temp)) {
            temp = temp.substring(0, temp.lastIndexOf("/"));
            if (!"".equals(temp))
                paths.add(temp);
        }
        Collections.reverse(paths);
        // 逐级遍历及创建文件夹
        for (String path : paths) {
            if (!exists(path)) {
                mkdir(path);
            }
        }
        return upload(src, destPath);
    }


    // 非连续创建
    public void mkdir(String path) throws IOException {
        checkConnected();
        try {
            sftpClient.mkdir(path);
        } catch (SftpException e) {
            if (e.getStatus() == SSH_FX_FAILURE) {
                LOG.error("文件夹创建失败, path = {}", path);
            }
            throw e;
        }
    }


    public boolean exists(String path) {
        checkConnected();
        try (InputStream ignored = sftpClient.read(path, SftpClient.OpenMode.Read)) {
            // just check if exists
        } catch (SftpException e) {
            if (e.getStatus() == SSH_FX_NO_SUCH_FILE) {
                return false;
            }
            LOG.error("SftpException", e);
            return false;
        } catch (IOException e) {
            LOG.error("IOException", e);
            return false;
        }
        return true;
    }
}
