package com.gv.steel.common.log.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = LogProperties.PREFIX)
public class LogProperties {
    public final static String PREFIX = "system.log";

    /**
     * 开启日志记录
     */
    private boolean enabled = true;

    /**
     * 放行字段，password,mobile,idcard,phone
     */
    @Value("${security.log.exclude-fields:password,mobile,idcard,phone}")
    private String[] excludeFields;

    /**
     * 请求报文最大存储长度
     */
    private Integer maxLength = 2000;
}
