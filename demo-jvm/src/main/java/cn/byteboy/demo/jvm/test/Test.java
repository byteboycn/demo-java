package cn.byteboy.demo.jvm.test;



/**
 * @author hongshaochuan
 * @date 2022/3/3
 */
public class Test {

    private static final Object lock = new Object();

    private static volatile int i = 0;

    private static final int end = 10;

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while (true) {
                synchronized (lock) {
                    if (i > end) {
                        lock.notify();
                        break;
                    }
                    System.out.println("Thread one: " + i++);
                    lock.notify();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Thread t2 = new Thread(() -> {
            while (true) {
                synchronized (lock) {
                    if (i > end) {
                        lock.notify();
                        break;
                    }
                    System.out.println("Thread two: " + i++);
                    lock.notify();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();
        System.out.println("main end");
    }

}