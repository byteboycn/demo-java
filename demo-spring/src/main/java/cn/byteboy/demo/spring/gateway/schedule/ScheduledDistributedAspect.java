package cn.byteboy.demo.spring.gateway.schedule;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author hongshaochuan
 */
@Aspect
@Component
public class ScheduledDistributedAspect {

    @Around("@annotation(scheduledDistributed)")
    public Object aroundAdvice(ProceedingJoinPoint pjp, ScheduledDistributed scheduledDistributed) throws Throwable {

        System.out.println("exec ScheduledDistributedAspect : " + pjp.getSignature().toLongString());

        return pjp.proceed();
    }
}
