package cn.byteboy.demo.jvm.test;


import cn.hutool.core.util.ArrayUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author hongshaochuan
 */
public class Main {

    public static void main(String[] args) {

//        Base base = Factory.create();
//
//
//        System.out.println(base instanceof Base);
//        System.out.println(base instanceof A);
//        System.out.println(base instanceof B);
//        System.out.println(base instanceof C);

        Base base = Factory.create(true).withA("a").build();

        System.out.println(base instanceof Base);
        System.out.println(base instanceof A);
        System.out.println(base instanceof B);
        System.out.println(base instanceof C);

        System.out.println(((A) base).a());
    }
}

class Factory {

    public static class Builder {

        boolean flag;

        A innerA;

        public Builder(boolean flag) {
            this.flag = flag;
        }

        public Builder withA(String a) {
            innerA = new A() {

                @Override
                public boolean success() {
                    return Builder.this.flag;
                }

                @Override
                public String a() {
                    return a;
                }
            };
            return this;
        }

        public Base build() {

            Base base = new Base() {
                @Override
                public boolean success() {

                    return Builder.this.flag;
                }
            };

            B innerB = new B() {
                @Override
                public String b() {
                    return "b";
                }

                @Override
                public boolean success() {
                    return false;
                }
            };

            Class<?>[] interfaces = ArrayUtil.addAll(innerA.getClass().getInterfaces(), innerB.getClass().getInterfaces());
            ClassLoader cl = Thread.currentThread().getContextClassLoader();

            return (Base) Proxy.newProxyInstance(cl, interfaces, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    return method.invoke(innerA, args);
                }
            });


        }
    }

    public static Builder create(boolean flag) {
        return new Builder(flag);
    }
}

interface Base {

    boolean success();

    default Base with(Base other) {
        return other;
    }
}

interface A extends Base {
    String a();
}

interface B extends Base {
    String b();
}

interface C extends Base {
    String c();
}