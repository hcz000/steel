package com.gv.steel.system.im.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = CIMProperties.PREFIX)
public class CIMProperties {
    public final static String PREFIX = "cim";
    /**
     * 消费者类型，redis、rocketmq
     */
    private String consumerType;
}
