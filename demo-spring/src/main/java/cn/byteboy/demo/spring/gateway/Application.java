package cn.byteboy.demo.spring.gateway;

import org.reactivestreams.Publisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author hongshaochuan
 * @date 2021/10/24
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
//        SpringApplication.run(Application.class, args);
        new Application().reactorTest();
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
