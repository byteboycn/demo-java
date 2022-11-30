package cn.byteboy.demo.spring.gateway;

import cn.byteboy.demo.spring.gateway.component.AService;
import cn.byteboy.demo.spring.gateway.component.BService;
import cn.byteboy.demo.spring.gateway.component.TopService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

/**
 * @author hongshaochuan
 */
@SpringBootTest
class ApplicationTest implements ApplicationContextAware {

    @Resource
    private TopService topService;

    @Autowired
    private AService aService;
//
    @MockBean
    private BService bService;

    private ApplicationContext applicationContext;
//
//    @Test
//    public void test() {
//        when(bService.b()).thenReturn("mock b");
//        System.out.println("test");
//
//        aService.a();
//    }

    @Test
    public void top() {
        when(bService.b()).thenReturn("mock b");
//        topService.top();
//        System.out.println(bService.b2());
        aService.a();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}