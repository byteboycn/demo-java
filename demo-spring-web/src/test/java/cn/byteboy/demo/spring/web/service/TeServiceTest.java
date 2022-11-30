package cn.byteboy.demo.spring.web.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @author hongshaochuan
 */
@SpringBootTest
public class TeServiceTest {

    @Resource
    private TeService teService;

    @MockBean
    private BService bService;


    @Test
    public void r1() {
        when(bService.r3()).thenReturn("mock b");
        teService.r1();
    }
}