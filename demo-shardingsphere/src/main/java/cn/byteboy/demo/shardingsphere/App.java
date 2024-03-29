package cn.byteboy.demo.shardingsphere;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * @author hongshaochuan
 */
@SpringBootApplication
@MapperScan("cn.byteboy.demo.shardingsphere.repository")
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
