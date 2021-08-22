package cn.byteboy.demo.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author hongshaochuan
 * @date 2021/8/18
 */
public class Producer extends Thread {

    private final KafkaProducer<Integer, String> producer;

    private final String topic;

    public Producer(String topic) {
        this.topic = topic;
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.KAFKA_SERVER_URL + ":" + KafkaProperties.KAFKA_SERVER_PORT);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "demoProducer1");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        this.producer = new KafkaProducer<>(props);
    }

    @Override
    public void run() {
        int i = 0;
        while (true) {
            String content = "Content_" + i;
            ProducerRecord<Integer, String> record = new ProducerRecord<>(topic, i, content);
            i++;
            try {
                producer.send(record).get();
                Thread.sleep(1);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        }
    }
}
