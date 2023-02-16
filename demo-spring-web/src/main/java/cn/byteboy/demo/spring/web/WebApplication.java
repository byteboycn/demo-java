package cn.byteboy.demo.spring.web;

import cn.byteboy.demo.spring.web.service.BService;
import cn.byteboy.demo.spring.web.service.TeService;
import cn.byteboy.demo.spring.web.service.TestService;
//import cn.byteboy.demo.spring.web.state.machine.BusinessService;
import cn.byteboy.demo.spring.web.state.machine.BusinessService;
import cn.byteboy.demo.spring.web.state.machine.EventEnum;
import cn.byteboy.demo.spring.web.state.machine.MyStateEvent;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.aop.framework.Advised;
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

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(WebApplication.class, args);

//        BService bean = context.getBean(BService.class);
//        if (bean instanceof Advised) {
//            System.out.println("proxy");
//            Object target = ((Advised) bean).getTargetSource().getTarget();
//            System.out.println(target);
//        }
//        System.out.println(bean);
//        bean.r3();

    }
}
