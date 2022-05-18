package cn.byteboy.demo.jvm.io;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hongshaochuan
 * @Date 2021/11/8
 */
public class SpeedRecorder<T extends Number> {

    // 采样周期 ms
    private final int period;

    // 采样时长 ms, -1 表示不受限制
    private final long duration;

    // 按照timestamp 自然排序
    private final Queue<Record> records = new PriorityQueue<>(Comparator.comparingLong(Record::getTimestamp));

    private long startTimestamp;

    private long endTimestamp;

    public SpeedRecorder(int period) {
        this(period, -1);
    }

    public SpeedRecorder(int period, long duration) {
        this.period = period;
        this.duration = duration;
    }

    public void start() {
        // 统计区间为左开右闭
        this.startTimestamp = System.currentTimeMillis();
    }

    public void end() {
        this.endTimestamp = System.currentTimeMillis();
    }

    public synchronized void record(T data) {
        records.add(new Record(System.currentTimeMillis(), data));
    }

    public List<PeriodResult> parse() {

        if (startTimestamp == 0L || endTimestamp == 0L) {
            throw new RuntimeException("run start and end first");
        }

        List<PeriodResult> resultList = new ArrayList<>();
        AtomicInteger resultIndex = new AtomicInteger(1);

        long l = startTimestamp;
        long r = startTimestamp + period;
        PeriodResult result = new PeriodResult(l, r, resultIndex.getAndIncrement());
        resultList.add(result);
        while (records.size() > 0) {
            Record record = records.poll();

            while (l < r) {
                // 防止startTimestamp时刻插入的数据
                long tempL = l == startTimestamp ? startTimestamp - 1 : l;
                if (tempL < record.getTimestamp() && record.getTimestamp() <= r) {
                    // handle
                    result.add(record.getData());
                    break;
                } else {
                    l = r;
                    r = Math.min(l + period, endTimestamp);
                    result = new PeriodResult(l, r, resultIndex.getAndIncrement());
                    resultList.add(result);
                }
            }
        }
        return resultList;
    }



    public class PeriodResult {

        private final long left;

        private final long right;

        private final long period;

        private final AtomicInteger counter = new AtomicInteger(0);

        private double sum = 0;

        private final int index;

        public PeriodResult(long left, long right, int index) {
            this.left = left;
            this.right = right;
            this.period = right - left;
            this.index = index;
        }

        public void add(T data) {
            counter.incrementAndGet();
            sum += data.doubleValue();
        }

        public double average() {
            if (counter.get() == 0) {
                return 0;
            }
            return sum / counter.get();
        }

        public long getPeriod() {
            return period;
        }

        public double getSum() {
            return sum;
        }
    }



    private class Record {
        private final long timestamp;

        private final T data;

        public Record(long timestamp, T data) {
            this.timestamp = timestamp;
            this.data = data;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public T getData() {
            return data;
        }
    }


}
