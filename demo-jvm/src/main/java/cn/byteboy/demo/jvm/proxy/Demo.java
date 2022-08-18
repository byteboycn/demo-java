package cn.byteboy.demo.jvm.proxy;

/**
 * @author hongshaochuan
 */
public class Demo implements IDemo {

    @Override
    public void say() {
        System.out.println("hello");
    }
}


interface IDemo {
    void say();
}
