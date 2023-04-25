package cn.byteboy.demo.spring.web.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author hongshaochuan
 */
@Slf4j
@Component
@RabbitListener(queues = MQConfig.TEST_QUEUE, concurrency = "1-4")
public class MQListener {

    @RabbitHandler(isDefault = true)
    public void nextNode(org.springframework.amqp.core.Message message) {
        String jsonMessage = new String(message.getBody(), StandardCharsets.UTF_8);
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        String queueName = message.getMessageProperties().getConsumerQueue();
        log.info("Received message node queue: {}, routingKey: {}, received json message: {}", queueName, routingKey, jsonMessage);
    }
}
