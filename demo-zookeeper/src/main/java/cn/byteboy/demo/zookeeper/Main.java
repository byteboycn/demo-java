package cn.byteboy.demo.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.data.Stat;

/**
 * @author hongshaochuan
 */
public class Main {

    public static void main(String[] args) throws Exception {
//        final CuratorFramework zkClient = CuratorFrameworkFactory.newClient("172.16.42.112:32181", new RetryNTimes(10, 5000));
        final CuratorFramework zkClient = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new RetryNTimes(10, 5000));

        zkClient.start();

        Stat stat = new Stat();
        byte[] ctx = zkClient.getData().storingStatIn(stat).forPath("/demo_ds");
        //获取节点内容为ctx
        System.out.println("data:" + new String(ctx));

        System.out.println(stat);

        zkClient.delete().deletingChildrenIfNeeded().forPath("/demo_ds");

        zkClient.close();
    }
}
