package cn.byteboy.demo.spring.web.listener;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hongshaochuan
 */
@Configuration
public class MQConfig {

    public static final String TEST_EXCHANGE = "test-exchange";
    public static final String TEST_QUEUE = "test-queue";

    @Bean
    Exchange testExchange() {
        return new TopicExchange(TEST_EXCHANGE);
    }

    @Bean
    Queue testQueue() {
        return new Queue(TEST_QUEUE);
    }

    @Bean
    Binding testBinding(Queue testQueue, Exchange testExchange) {
        return BindingBuilder.bind(testQueue).to(testExchange).with("#").noargs();
    }

}
