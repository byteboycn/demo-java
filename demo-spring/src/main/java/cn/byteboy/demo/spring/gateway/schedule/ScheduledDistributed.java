package cn.byteboy.demo.spring.gateway.schedule;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分布式调度注解
 *
 * @author caixinjiang
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ScheduledDistributed {

    /**
     * 分布式锁的名称, 默认是被注解的方法签名
     */
    String value() default "";

    /**
     * 最小执行秒数. 调度执行时间小于此配置, 将睡眠. 避免分布式机器时间差, 导致的重复执行
     */
    int minExecutionSeconds() default 10;
}
