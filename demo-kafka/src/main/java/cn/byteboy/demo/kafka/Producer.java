package cn.byteboy.demo.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.Properties;

/**
 * @author hongshaochuan
 * @date 2021/8/18
 */
public class Producer extends Thread {

    private final KafkaProducer<Integer, String> producer;

    public Producer() {

        Properties props = new Properties();
        this.producer = new KafkaProducer<>(props);
    }

    @Override
    public void run() {
        super.run();
    }
}
