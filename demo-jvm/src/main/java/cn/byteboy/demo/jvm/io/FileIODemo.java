package cn.byteboy.demo.jvm.io;

import cn.hutool.core.io.FileUtil;
import cn.hutool.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * @author hongshaochuan
 * @Date 2021/11/5
 *
 * 通过几种方式测试io性能
 */
public class FileIODemo {

    private static final Logger LOG = LoggerFactory.getLogger(FileIODemo.class);

    // 顺序读性能
    private static final String TEST_READ_COMMAND = "fio --filename=test -iodepth=16 -ioengine=libaio --direct=1 --rw=read --bs=2m --size=512m --numjobs=4 --runtime=10 --group_reporting --name=test-read";

    // 顺序写性能
    private static final String TEST_WRITE_COMMAND = "fio --filename=test -iodepth=16 -ioengine=libaio -direct=1 -rw=write -bs=1m -size=512m -numjobs=4 -runtime=20 -group_reporting -name=test-write";

    private static final String TEST_DIR = System.getProperty("user.home") + File.separator + "FileIODemo";

    public FileIODemo() {
        FileUtil.mkdir(TEST_DIR);

        Runtime.getRuntime().addShutdownHook(new Thread(this::destroy));
    }

    public void destroy() {
        FileUtil.del(TEST_DIR);
    }

    public static void main(String[] args) throws IOException {
        FileIODemo demo = new FileIODemo();
        demo.testDiskMaxSpeed();
        demo.test();
    }

    public void test() {

//        new RandomAccessFile(new File("data"), "rw")
    }

    /**
     * 使用fio测试硬盘的顺序读写速度
     *
     * @throws IOException
     */
    public void testDiskMaxSpeed() throws IOException {
        String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if (!os.contains("linux")) {
            LOG.warn("using linux run fio to test disk max speed");
            return;
        }
        Process process;
        try {
            process = Runtime.getRuntime().exec(TEST_READ_COMMAND);
        } catch (Exception e) {
            LOG.error("无法运行fio，请检查是否安装fio，或者直接在命令行执行");
            return;
        }

        LOG.info("执行fio测试硬盘速度");
        InputStream is = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        String line;
        String readRes = null;
        String writeRes = null;
        while ( (line = reader.readLine()) != null ) {
            readRes = line;
        }
        reader.close();


        process = Runtime.getRuntime().exec(TEST_WRITE_COMMAND);
        is = process.getInputStream();
        reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        while ( (line = reader.readLine()) != null ) {
            writeRes = line;
        }
        reader.close();

        // 只取结果行
        LOG.info(readRes);
        LOG.info(writeRes);
        LOG.warn("测试结果仅供参考，实际测试发现直接在命令行执行速度会更快");
    }
}
