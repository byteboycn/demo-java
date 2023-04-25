package cn.byteboy.demo.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author hongshaochuan
 */
public class Main {

    private static final String TEST_EXCHANGE = "test-exchange";

    private static final String TEST_QUEUE = "test-queue";


    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("root");
        factory.setPassword("root");
        factory.setVirtualHost("/");
        factory.setPort(5672);
        factory.setAutomaticRecoveryEnabled(true);


        Connection conn = factory.newConnection();
        final Channel channel = conn.createChannel();

        prepare(channel);

        new Thread(() -> sender(channel)).start();

        consumeMessage(channel);

//        channel.close();
//        conn.close();

//        long start = System.currentTimeMillis();
//        for (int i = 0; i < 100000; i++) {
//            sendMessage(channel, String.valueOf(i));
//        }
//        long end = System.currentTimeMillis();
//        System.out.println("cost time:" + (end - start));
//        System.out.println("done");
    }

    static void sender(Channel channel) {
        Scanner scanner = new Scanner(System.in);
        String sign = null;
        while (!"end".equals(sign)) {
            sign = scanner.nextLine();
            System.out.println("[" + Thread.currentThread().getName() + "] send message: " + sign);
            try {
                sendMessage(channel, sign);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("end sender");
    }

    static void prepare(Channel channel) throws IOException {
        channel.exchangeDeclare(TEST_EXCHANGE, BuiltinExchangeType.TOPIC, true);
        channel.queueDeclare(TEST_QUEUE, true, false, false, null);
        channel.queueBind(TEST_QUEUE, TEST_EXCHANGE, "#");
    }

    static void sendMessage(Channel channel, String message) throws IOException {
        channel.basicPublish(TEST_EXCHANGE, "#",
                new AMQP.BasicProperties.Builder()
                        .contentType("text/plain")
                        .deliveryMode(2)
                        .build(),
                message.getBytes());
    }

    static void consumeMessage(Channel channel) throws IOException {
        channel.basicConsume(TEST_QUEUE, false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("[" + Thread.currentThread().getName() + "] receive message: " + new String(body));
                long deliveryTag = envelope.getDeliveryTag();
                channel.basicAck(deliveryTag, false);

            }
        });
    }
}
