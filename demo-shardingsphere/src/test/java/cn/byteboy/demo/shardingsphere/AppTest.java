package cn.byteboy.demo.shardingsphere;

import cn.byteboy.demo.shardingsphere.service.ExampleService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author hongshaochuan
 */
@SpringBootTest
public class AppTest {

    @Resource
    private ExampleService exampleService;
    @Test
    void t1() throws Exception {
        exampleService.run();
    }
}
