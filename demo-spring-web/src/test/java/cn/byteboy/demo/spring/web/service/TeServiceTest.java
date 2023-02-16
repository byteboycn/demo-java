package cn.byteboy.demo.spring.web.service;

import cn.byteboy.demo.spring.web.entity.TableAEntity;
import cn.byteboy.demo.spring.web.mapper.TableAMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import java.sql.Driver;

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

    @Resource
    private TableAMapper tableAMapper;
    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @Test
    public void r1() {
        when(bService.r3()).thenReturn("mock b");
        teService.r1();
    }

    @Test
    public void t2() {
        TableAEntity tableA = tableAMapper.selectById(1);
        System.out.println(tableA);
    }

    @Test
    public void t3() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
    }
}