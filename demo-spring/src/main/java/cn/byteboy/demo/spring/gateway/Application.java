package cn.byteboy.demo.spring.gateway;

import cn.byteboy.demo.spring.gateway.component.AService;
import cn.byteboy.demo.spring.gateway.schedule.ScheduleDemo;
import org.reactivestreams.Publisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author hongshaochuan
 * @date 2021/10/24
 */
@SpringBootApplication
//@EnableAspectJAutoProxy(exposeProxy = true)
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        AService bean = context.getBean(AService.class);
        System.out.println(bean);
    }

    public void reactorTest() {
        Flux.fromIterable(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"))
                .concatMap(new Function<String, Publisher<?>>() {
                    @Override
                    public Publisher<?> apply(String s) {
                        return Mono.just(s).filterWhen(s1 -> {
                            if ("5".equals(s1)) {
                                return Mono.just(true);
                            } else {
                                return Mono.just(false);
                            }
                        });
                    }
                })
                .next()
                .subscribe(System.out::println);
    }
}
