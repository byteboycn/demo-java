package cn.byteboy.demo.spring.web.controller;

import cn.byteboy.demo.spring.web.entity.TableAEntity;
import cn.byteboy.demo.spring.web.listener.MQConfig;
import cn.byteboy.demo.spring.web.mapper.TableAMapper;
import cn.byteboy.demo.spring.web.service.TestService;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.sql.Wrapper;
import java.util.Date;
import java.util.List;

/**
 * @author hongshaochuan
 * @date 2022/3/23
 */
@RestController
@RequestMapping("/test")
public class TestController {


    @Resource
    private TestService testService;

    @Resource
    private TableAMapper tableAMapper;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("/t/{value}")
    @Transactional
    public String t(@PathVariable("value") String value) {

        List<TableAEntity> tableAEntities = tableAMapper.selectList(
                Wrappers.<TableAEntity>lambdaQuery().lt(TableAEntity::getADate, getToday()));
        System.out.println(tableAEntities.size());

//        TableAEntity newEntity = new TableAEntity();
//        newEntity.setAId(100);
//        newEntity.setAName("100Name");
//        tableAMapper.insert(newEntity);
//
//        tableAMapper.deleteById(newEntity.getAId());

        return "ok";
    }

    @RequestMapping("/echo/{echo}")
    public String echo(@PathVariable("echo") String echo) {
        return echo;
    }

    @RequestMapping("/mq/{msg}")
    public String mq(@PathVariable("msg") String msg) {
        rabbitTemplate.convertAndSend(MQConfig.TEST_EXCHANGE, "#", msg);
        return "ok";
    }


    private static Date getToday() {
        return DateUtil.parse(DateUtil.today(), DatePattern.NORM_DATE_PATTERN).toJdkDate();
    }
}
