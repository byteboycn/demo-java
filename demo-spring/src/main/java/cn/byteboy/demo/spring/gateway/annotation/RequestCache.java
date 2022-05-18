package cn.byteboy.demo.spring.gateway.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author hongshaochuan
 * @date 2022/3/23
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestCache {

    String name();

    String key() default "";
}
