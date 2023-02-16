package cn.byteboy.demo.shardingsphere.sharding;

//import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.apache.shardingsphere.sharding.spi.KeyGenerateAlgorithm;

import java.util.Properties;

/**
 * @author hongshaochuan
 */
public class MyKeyGenerator implements KeyGenerateAlgorithm {

    @Override
    public Comparable<?> generateKey() {
//        return IdWorker.getId();
        return 1L;
    }

    @Override
    public Properties getProps() {
        return null;
    }

    @Override
    public void init(Properties properties) {

    }

    @Override
    public String getType() {
        return "MY_KEY_GENERATOR";
    }

    public static void main(String[] args) {
//        String shardingValue = "fucka";
//        Long shardingValue = 827932971787878401L;
//        long abs = Math.abs((long) shardingValue.hashCode());
//        System.out.println(abs);
//        System.out.println(shardingValue.hashCode());
        int i = 0;
        do {

            System.out.println(i);
            i++;
        } while (i != 10);

    }
}
