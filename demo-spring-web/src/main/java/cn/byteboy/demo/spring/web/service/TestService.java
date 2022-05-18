package cn.byteboy.demo.spring.web.service;

import cn.byteboy.demo.spring.web.annotation.RequestCache;
import org.springframework.stereotype.Service;

/**
 * @author hongshaochuan
 * @date 2022/3/23
 */
@Service
public class TestService {

    @RequestCache(name = "T3", key = "#id")
    public String test3(Long id) {
        System.out.println("execute test3");
        return "test3";
    }


    @RequestCache(name = "T2", key = "#id")
    public String test2() {
        System.out.println("execute test2");
        return "test2";
    }

}
