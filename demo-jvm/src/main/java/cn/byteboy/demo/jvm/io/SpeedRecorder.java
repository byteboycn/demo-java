package cn.byteboy.demo.jvm.io;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

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

    public SpeedRecorder(int period) {
        this(period, -1);
    }

    public SpeedRecorder(int period, long duration) {
        this.period = period;
        this.duration = duration;
    }

    public void start() {
        // 统计区间为左开右闭
        this.startTimestamp = System.currentTimeMillis() - 1;
    }

    public void record(T data) {
        records.add(new Record(System.currentTimeMillis(), data));
    }

    public void log() {
        if (startTimestamp == 0L) {
            return;
        }

        long l = startTimestamp;
        long r = startTimestamp + period;

        while (records.size() > 0) {
            Record record = records.poll();

            while (true) {
                if (l < record.getTimestamp() && record.getTimestamp() <= r) {
                    // handle

                    break;
                } else {
                    l = r;
                    r = l + period;
                }
            }


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
