package cn.byteboy.demo.jvm.sshd.sftp;

import cn.hutool.extra.ftp.Ftp;
import org.apache.sshd.sftp.client.SftpClient;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author hongshaochuan
 * @Date 2021/9/26
 */
public class Demo {

    private static final String user = "sftp";

    private static final String host = "47.113.122.62";
//    private static final String host = "191.113.122.62";

    private static final int port = 22;

    private static final String password = "Wxsk_2020";


    public static void main(String[] args) throws IOException {

        new Ftp("");
        new Demo().test();


//        String path = "/sftp/111/222/333/aaa.jar";



//        int i = path.lastIndexOf("/");
//        StringBuffer sb = new StringBuffer(path);
//        sb.lastIndexOf("/");

//        System.out.println(path.substring(0, i));

    }

    public void test() throws IOException {
        DefaultSftpManager sftpManager = new DefaultSftpManager(user, host, port, password);
        sftpManager.connect();

//        System.out.println(sftpManager.exists("/sftp1"));
//        SftpClient client = sftpManager.getClient();

//        readString(client);

        sftpManager.uploadAndMkdirIfNecessary(Paths.get("D:\\actibpm.jar"), "/sftp/111/222/333/aaa.jar");
//        sftpManager.mkdir("/sftp/fuck");

        sftpManager.close();
    }

    public void readString(SftpClient client) throws IOException {
        InputStream is = client.read("/sftp/11.txt", SftpClient.OpenMode.Read);
        int bufSize = 4096;
        byte[] buf = new byte[bufSize];
        int readLen;
        ByteBuffer byteBuffer = ByteBuffer.allocate(100 * 1024);
        try (BufferedInputStream bis = new BufferedInputStream(is, bufSize)) {
            while ((readLen = bis.read(buf)) != -1) {
                byteBuffer.put(buf, 0, readLen);
            }
        }
        byteBuffer.flip();
        Charset charset = StandardCharsets.UTF_8;
        CharBuffer decode = charset.decode(byteBuffer);
        System.out.println(decode);
    }








}
