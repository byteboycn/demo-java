package cn.byteboy.demo.spring.gateway.controller;

import cn.byteboy.demo.spring.gateway.AopDemo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hongshaochuan
 * @date 2022/3/23
 */
@RestController
@RequestMapping("/test")
public class TestController {


    @Resource
    private AopDemo aopDemo;

    @RequestMapping("/t/{value}")
    public String t(@PathVariable("value") String value) {
        aopDemo.test3(Long.valueOf(value));
        return "t";
    }
}
