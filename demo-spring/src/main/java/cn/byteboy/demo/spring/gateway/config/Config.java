package cn.byteboy.demo.spring.gateway.config;

import cn.byteboy.demo.spring.gateway.handler.RouteHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hongshaochuan
 * @date 2021/10/24
 */
@Configuration(proxyBeanMethods = false)
public class Config {

    @Bean
    public RouteHandlerMapping routeHandlerMapping() {
        return new RouteHandlerMapping();
    }
}
