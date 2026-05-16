package com.gv.steel.common.datasource;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gv.steel.common.datasource.aop.MasterSlaveDataSourceAop;
import com.gv.steel.common.datasource.datasource.MasterSlaveDataSource;
import com.gv.steel.common.datasource.properties.MasterSlaveDataSourceProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

@Configuration(proxyBeanMethods = false)
@AutoConfiguration(before = org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(MasterSlaveDataSourceProperties.class)
public class DataSourceAutoConfiguration {

    @Bean
    @ConditionalOnProperty(value = "spring.master-slave.datasource.enabled", havingValue = "true")
    public DataSource masterSlaveDataSource(MasterSlaveDataSourceProperties properties) {
        MasterSlaveDataSource dataSource = new MasterSlaveDataSource();

        // 主数据库
        dataSource.setDefaultTargetDataSource(new HikariDataSource(new HikariConfig(properties.getMaster())));

        // 从数据库
        Map<Object, Object> slaveDataSource = Maps.newHashMap();

        // 从数据库 Key
        dataSource.setSlaveKeys(Lists.newArrayList());

        for (Map.Entry<String, Properties> entry : properties.getSlaves().entrySet()) {

            if (slaveDataSource.containsKey(entry.getKey())) {
                throw new IllegalArgumentException("存在同名的从数据库定义：" + entry.getKey());
            }

            slaveDataSource.put(entry.getKey(), new HikariDataSource(new HikariConfig(entry.getValue())));

            dataSource.getSlaveKeys().add(entry.getKey());
        }

        // 设置从库
        dataSource.setTargetDataSources(slaveDataSource);

        return dataSource;
    }

    @Bean
    @ConditionalOnProperty(value = "spring.master-slave.datasource.enabled", havingValue = "true")
    public MasterSlaveDataSourceAop masterSlaveDataSourceAop() {
        return new MasterSlaveDataSourceAop();
    }

}
