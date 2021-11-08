package cn.byteboy.demo.jvm.io;

import cn.hutool.core.io.FileUtil;
import cn.hutool.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

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

    private static final String TEST_DIR = System.getProperty("user.home") + File.separator + "FileIODemo" + File.separator;

    private static final int threadNum = Runtime.getRuntime().availableProcessors();

    private static final ExecutorService executor = Executors.newFixedThreadPool(threadNum, r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    });




    public FileIODemo() {
        FileUtil.mkdir(TEST_DIR);

//        Runtime.getRuntime().addShutdownHook(new Thread(this::destroy));
    }

    public void destroy() {
        FileUtil.del(TEST_DIR);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        int perSize = 4 * 1024; // 4k
        long counter = 1024 * 100 * 3;

        FileIODemo demo = new FileIODemo();
//        demo.testDiskMaxSpeed();
//        demo.test(perSize, counter);
        demo.single(perSize, counter);
    }

    public void single(int perSize, long counter) throws IOException {
        long capacity = perSize * counter;
        ByteBuffer data = ByteBuffer.wrap(new byte[perSize]);
        RandomAccessFile file = new RandomAccessFile(new File(TEST_DIR + "data_single"), "rw");
        FileChannel fileChannel = file.getChannel();

        long start = System.currentTimeMillis();
        for (int i = 0; i < counter; i++) {
            fileChannel.write(data);
            data.flip();
        }
        fileChannel.close();
        file.close();
        long end = System.currentTimeMillis();

        long speed = capacity / (end - start) * 1000;
        LOG.info("write data: {}, cost:{}ms, speed: {}/s", FileUtil.readableFileSize(capacity), end - start, FileUtil.readableFileSize(speed));

    }

    public void test(int perSize, long counter) throws IOException, InterruptedException {


//        byte[] data = new byte[perSize];
        ByteBuffer data = ByteBuffer.wrap(new byte[perSize]);
        long capacity = data.capacity() * counter; // 2g
        // 这里简化模型，粗暴的使每个线程写入相同的数据量
        if (counter % threadNum != 0) {
            LOG.error("为了保证每个线程写入相同的数据量，请调整写入量, 当前写入次数：{}，线程数：{}", counter, threadNum);
            return;
        }
        RandomAccessFile file = new RandomAccessFile(new File(TEST_DIR + "data"), "rw");
        FileChannel fileChannel = file.getChannel();
        long start = System.currentTimeMillis();

        final Object lock = new Object();
        AtomicLong position = new AtomicLong(0);
        CountDownLatch latch = new CountDownLatch(threadNum);
        for (int i = 0; i < threadNum; i++) {
            executor.execute(() -> {
                long times = counter / threadNum;
                for (long j = 0; j < times; j++) {
//                    synchronized (lock) {
//                        try {
//                            fileChannel.write(data, position.getAndAdd(data.capacity()));
//                            data.flip();    // 使data重复读取
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
                    try {
                        fileChannel.write(ByteBuffer.wrap(new byte[4 * 1024]), position.getAndAdd(4 * 1024));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                latch.countDown();
            });
        }

        latch.await();
        long end = System.currentTimeMillis();

        long speed = capacity / (end - start) * 1000;
        LOG.info("write data: {}, cost:{}ms, speed: {}/s", FileUtil.readableFileSize(capacity), end - start, FileUtil.readableFileSize(speed));

        fileChannel.close();
        file.close();

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
