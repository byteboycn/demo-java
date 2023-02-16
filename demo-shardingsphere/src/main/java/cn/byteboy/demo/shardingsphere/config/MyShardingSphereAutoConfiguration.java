//package cn.byteboy.demo.shardingsphere.config;
//
//import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
//import org.apache.shardingsphere.infra.config.mode.ModeConfiguration;
//import org.apache.shardingsphere.infra.config.rule.RuleConfiguration;
//import org.apache.shardingsphere.spring.boot.datasource.DataSourceMapSetter;
//import org.apache.shardingsphere.spring.boot.prop.SpringBootPropertiesConfiguration;
//import org.apache.shardingsphere.spring.boot.rule.LocalRulesCondition;
//import org.apache.shardingsphere.spring.boot.schema.DatabaseNameSetter;
//import org.apache.shardingsphere.spring.transaction.TransactionTypeScanner;
//import org.springframework.beans.factory.ObjectProvider;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.AutoConfigureBefore;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.EnvironmentAware;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Conditional;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//
//import javax.sql.DataSource;
//import java.sql.SQLException;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
///**
// * @author hongshaochuan
// */
//@Configuration
////@EnableConfigurationProperties(SpringBootPropertiesConfiguration.class)
//@AutoConfigureBefore(DataSourceAutoConfiguration.class)
//public class MyShardingSphereAutoConfiguration implements EnvironmentAware {
//
//    private String databaseName;
//
////    private final SpringBootPropertiesConfiguration props;
//
//    private final Map<String, DataSource> dataSourceMap = new LinkedHashMap<>();
//
//    @Bean
//    @Conditional(LocalRulesCondition.class)
//    @Autowired(required = false)
//    public DataSource shardingSphereDataSource(final ObjectProvider<List<RuleConfiguration>> rules, final ObjectProvider<ModeConfiguration> modeConfig) throws SQLException {
//        Collection<RuleConfiguration> ruleConfigs = Optional.ofNullable(rules.getIfAvailable()).orElseGet(Collections::emptyList);
//        return ShardingSphereDataSourceFactory.createDataSource(databaseName, modeConfig.getIfAvailable(), dataSourceMap, ruleConfigs, props.getProps());
//    }
//
//    /**
//     * Get data source bean from registry center.
//     *
//     * @param modeConfig mode configuration
//     * @return data source bean
//     * @throws SQLException SQL exception
//     */
//    @Bean
//    @ConditionalOnMissingBean(DataSource.class)
//    public DataSource dataSource(final ModeConfiguration modeConfig) throws SQLException {
//        return !dataSourceMap.isEmpty() ? ShardingSphereDataSourceFactory.createDataSource(databaseName, modeConfig, dataSourceMap, Collections.emptyList(), props.getProps())
//                : ShardingSphereDataSourceFactory.createDataSource(databaseName, modeConfig);
//    }
//
//    @Bean
//    public TransactionTypeScanner transactionTypeScanner() {
//        return new TransactionTypeScanner();
//    }
//
//    @Override
//    public final void setEnvironment(final Environment environment) {
//        dataSourceMap.putAll(DataSourceMapSetter.getDataSourceMap(environment));
//        databaseName = DatabaseNameSetter.getDatabaseName(environment);
//    }
//}
