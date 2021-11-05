package cn.byteboy.demo.jvm.io;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * @author hongshaochuan
 * @Date 2021/11/3
 */
public class Demo {
    private static final Logger LOG = LoggerFactory.getLogger(Demo.class);

    private static final String CONTENT = "content";

    private static final String DIR = "D:\\aa\\bb\\";

    // 4k
    private static final byte[] bytes = new byte[4 * 1024];

    public static void main(String[] args) throws IOException {
//        long lineCount = 60000000;
//        new Demo().createLinesFile(lineCount, new File("D:\\aa\\bb\\d.txt"));

        new Demo().createSmallFiles(1 * 1024 * 25);
    }

    public void createSmallFiles(long count) throws IOException {
        ensureDirExist(DIR);

        long start = System.currentTimeMillis();

        for (long i = 0; i < count; i++) {
            BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(DIR + i));
            writer.write(bytes);
            writer.flush();
            writer.close();
        }

        long end = System.currentTimeMillis();
        LOG.info("cost time:{} ms, file size:{}", end - start, FileUtil.readableFileSize(count * bytes.length));
    }

    public void createLinesFile(long lineCount, File file) throws IOException {
        // 创建父文件夹 （如果没有的话）
        FileUtil.mkParentDirs(file);

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        long start = System.currentTimeMillis();

        StringBuilder sb = new StringBuilder();
        long lineCache = 100000;
        for (long i = 0; i < lineCount; i++) {
            sb.append(i + 1).append(" -- ").append(CONTENT).append("\n");
            if (i % lineCache == 0 && sb.length() > 0) {
                writer.write(sb.toString());
                sb.setLength(0);
            }
        }
        if (sb.length() > 0) {
            writer.write(sb.toString());
            sb.setLength(0);
        }
        writer.close();

        long end = System.currentTimeMillis();


        LOG.info("cost time:{} ms, file size:{}", end - start, FileUtil.readableFileSize(file));
    }


    public void ensureDirExist(String path) {
        FileUtil.mkParentDirs(path);
    }

}
