package cn.byteboy.demo.spring.web.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hongshaochuan
 */
@Service
@Getter
@Slf4j
public class TeService extends AbstractService {

    @Resource
    private TestService t1;


}
