package cn.byteboy.demo.spring.gateway.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author hongshaochuan
 * @date 2022/1/8
 */
@Aspect
@Component
public class A1Aspect {

    @Pointcut("@annotation(cn.byteboy.demo.spring.gateway.annotation.A1)")
    public void pointCut() {}

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        String arg = (String) pjp.getArgs()[0];
        return pjp.proceed(new Object[]{arg + ": in proxy"});
    }
}
