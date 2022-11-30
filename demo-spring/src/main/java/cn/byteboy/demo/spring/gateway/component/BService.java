package cn.byteboy.demo.spring.gateway.component;

import org.springframework.stereotype.Component;

/**
 * @author hongshaochuan
 */
@Component
public class BService {


    public String b() {
        return "b service";
    }

    public String b2() {
        return "b2";
    }
}
