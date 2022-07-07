package cn.byteboy.demo.spring.web;

import cn.byteboy.demo.spring.web.service.TeService;
import cn.byteboy.demo.spring.web.service.TestService;
//import cn.byteboy.demo.spring.web.state.machine.BusinessService;
import cn.byteboy.demo.spring.web.state.machine.BusinessService;
import cn.byteboy.demo.spring.web.state.machine.EventEnum;
import cn.byteboy.demo.spring.web.state.machine.MyStateEvent;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

/**
 * @author hongshaochuan
 * @date 2022/3/23
 */
@SpringBootApplication
@MapperScan(basePackages = "cn.byteboy.demo.spring.web.mapper")
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);

//        Message<EventEnum> message = MessageBuilder.withPayload(EventEnum.E1).setHeader("pojo", null).build();
//        context.publishEvent(new MyStateEvent(message));
//        System.out.println(bean);

    }
}
