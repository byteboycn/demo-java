package cn.byteboy.demo.spring.web.service;

import cn.byteboy.demo.spring.web.annotation.RequestCache;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author hongshaochuan
 */
@Service
public class BService {

    @Transactional
    public String r3() {
        return "r3 value";
    }
}
