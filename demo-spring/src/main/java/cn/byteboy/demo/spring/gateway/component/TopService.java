package cn.byteboy.demo.spring.gateway.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author hongshaochuan
 */
@Component
public class TopService {

    @Resource
    private AService aService;

    public void top() {
        System.out.println("top run");
        aService.a();
    }
}
