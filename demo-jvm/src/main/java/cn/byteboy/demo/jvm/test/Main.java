package cn.byteboy.demo.jvm.test;


import cn.hutool.core.util.ArrayUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author hongshaochuan
 */
public class Main {

    public static void main(String[] args) throws Exception {
        ThreadLocal<String> ctx = new InheritableThreadLocal<>();
        ctx.set("value-set-in-parent");
        System.out.println("[parent thread] set " + ctx.get());

        Thread t = new Thread(() -> {
            System.out.println("[child thread] get " + ctx.get());
        });
        t.start();
        t.join();


    }
}


