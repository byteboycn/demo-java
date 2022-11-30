package cn.byteboy.demo.spring.gateway.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author hongshaochuan
 */
@Component
public class AService {

    @Resource
    private BService bService;

    @Async
    public void a() {
        System.out.println("run b");
        System.out.println(bService.b());
    }
}
