package cn.byteboy.demo.spring.web.service;

import cn.byteboy.demo.spring.web.entity.TableAEntity;
import cn.byteboy.demo.spring.web.mapper.TableAMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author hongshaochuan
 */
@Service
@Getter
@Slf4j
public class TeService extends AbstractService {

    @Resource
    private TestService t1;

    @Lazy
    @Resource
    private TeService self;
    @Resource
    private TableAMapper tableAMapper;
    @Resource
    private BService bService;

    @Transactional
    public void r1() {
        try {
            System.out.println(bService.r3());
        } catch (Exception e) {
            log.error("fuck err", e);
        }
    }

    @Transactional
    public void r2() {
        TableAEntity entity = new TableAEntity();
        entity.setAId(101);
        entity.setAName("rollback");
        entity.setADate(new Date());
        tableAMapper.insert(entity);
        int a = 1/0;
    }


}
