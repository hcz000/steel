package com.gv.steel.common.feign.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(
        prefix = "rest-template"
)
public class RestTemplateProperties {
    /**
     * 最大链接数
     */
    private int maxTotal = 80000;
    /**
     * 同路由最大并发数
     */
    private int maxPerRoute = 50000;
    /**
     * 读取超时时间 ms
     */
    private int readTimeout = 60000;
    /**
     * 链接超时时间 ms
     */
    private int connectTimeout = 30000;
}
