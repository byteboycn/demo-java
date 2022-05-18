package cn.byteboy.demo.spring.web.service;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * @author hongshaochuan
 */
@Slf4j
public abstract class AbstractService {

    @Resource
    private TestService t0;
}
