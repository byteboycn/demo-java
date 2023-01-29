package cn.byteboy.demo.shardingsphere.sharding;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.apache.shardingsphere.sharding.spi.KeyGenerateAlgorithm;

import java.util.Properties;

/**
 * @author hongshaochuan
 */
public class MyKeyGenerator implements KeyGenerateAlgorithm {

    @Override
    public Comparable<?> generateKey() {
        return IdWorker.getId();
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
}
