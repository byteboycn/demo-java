package cn.byteboy.demo.spring.web;

import cn.byteboy.demo.spring.web.service.TeService;
import cn.byteboy.demo.spring.web.service.TestService;
//import cn.byteboy.demo.spring.web.state.machine.BusinessService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author hongshaochuan
 * @date 2022/3/23
 */
@SpringBootApplication
public class WebApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(WebApplication.class, args);
//        BusinessService bean = context.getBean(BusinessService.class);
//        bean.b1();
//        System.out.println(bean);

    }
}
