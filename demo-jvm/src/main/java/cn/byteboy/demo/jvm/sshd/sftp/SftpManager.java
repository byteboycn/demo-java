package cn.byteboy.demo.jvm.sshd.sftp;

import org.apache.sshd.sftp.client.SftpClient;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;

/**
 * @author hongshaochuan
 * @Date 2021/9/26
 */
public interface SftpManager extends Closeable {

    void connect() throws IOException;

    SftpClient getClient() throws IOException;

    /**
     * 检查对应path的文件或文件夹是否存在
     *
     * @param path
     * @return
     */
    boolean exists(String path);


    long uploadAndMkdirIfNecessary(Path src, String destPath) throws IOException;

    long upload(Path src, String destPath) throws IOException;
}
