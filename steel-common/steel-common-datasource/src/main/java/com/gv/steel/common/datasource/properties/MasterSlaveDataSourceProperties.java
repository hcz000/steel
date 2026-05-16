package com.gv.steel.common.datasource.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
import java.util.Properties;

@Data
@ConfigurationProperties(prefix = MasterSlaveDataSourceProperties.prefix)
public class MasterSlaveDataSourceProperties {
    public final static String prefix = "spring.master-slave.datasource";
    /**
     * 是否启用主从数据源
     */
    private boolean enabled = false;

    /**
     * 主数据源配置
     */
    private Properties master;

    /**
     * 从数据源配置
     */
    private Map<String, Properties> slaves;
}
