package cn.byteboy.demo.spring.gateway;

import cn.byteboy.demo.spring.gateway.annotation.A1;
import cn.byteboy.demo.spring.gateway.annotation.RequestCache;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author hongshaochuan
 * @date 2022/1/8
 */
@Component
public class AopDemo {

    @Lazy
    @Autowired
    private AopDemo self;


    @A1
    public void test1(String arg) {
        System.out.println("run test1, arg: " + arg);
//        AopDemo s = (AopDemo) AopContext.currentProxy();
        self.test2("test2");
//        bridge(arg);
//        test2("test2");
    }


    private void bridge(String arg) {
        self.test2(arg);
    }

    @A1
    public void test2(String arg) {
        System.out.println("run test2, arg: " + arg);
    }


    @RequestCache(name = "T3", key = "#id")
    public String test3(Long id) {

        return "test3";
    }
}
