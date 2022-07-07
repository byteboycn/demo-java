package cn.byteboy.demo.spring.web.controller;

import cn.byteboy.demo.spring.web.entity.TableAEntity;
import cn.byteboy.demo.spring.web.mapper.TableAMapper;
import cn.byteboy.demo.spring.web.service.TestService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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

    @RequestMapping("/t/{value}")
    public String t(@PathVariable("value") String value) {

        List<TableAEntity> tableAEntities = tableAMapper.selectList(null);

        System.out.println(tableAEntities.size());
        return "ok";
    }
}
