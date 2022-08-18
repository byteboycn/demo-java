package cn.byteboy.demo.jvm.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author hongshaochuan
 */
public class Main {
    public static void main(String[] args) {

        Demo target = new Demo();
        Class<?>[] interfaces = Demo.class.getInterfaces();

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        IDemo demoProxy = (IDemo) Proxy.newProxyInstance(cl, interfaces, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("demo proxy exec");
                return method.invoke(target, args);
            }
        });
        demoProxy.say();
        System.out.println("proxy:" + demoProxy.toString());

    }
}
