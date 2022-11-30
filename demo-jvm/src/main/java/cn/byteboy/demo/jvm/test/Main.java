package cn.byteboy.demo.jvm.test;


import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;

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
//        RandomUtil.randomInt(0, -1);
        List<Object> objects = Collections.singletonList(null);
        System.out.println(objects);
    }
}


