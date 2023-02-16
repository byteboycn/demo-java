package cn.byteboy.demo.shardingsphere;

import cn.byteboy.demo.shardingsphere.entity.Order;
import cn.byteboy.demo.shardingsphere.repository.OrderRepository;
import cn.byteboy.demo.shardingsphere.service.ExampleService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hongshaochuan
 */
@SpringBootTest
public class AppTest {

    @Resource
    private ExampleService exampleService;
    @Resource
    private OrderRepository orderRepository;
    @Test
    void t1() throws Exception {
        exampleService.run();
    }

    @Test
    void t2() {
        List<Order> orders = orderRepository.selectAll();
        System.out.println(orders);
    }

}
