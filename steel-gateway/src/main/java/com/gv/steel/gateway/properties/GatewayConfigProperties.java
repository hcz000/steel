package com.gv.steel.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.List;

@Data
@RefreshScope
@ConfigurationProperties("gateway")
public class GatewayConfigProperties {
    /**
     * 密码解密key
     */
    private String encodeKey;

    /**
     * 解密向量
     */
    private String iv;

    /**
     * 不需要校验的验证码的客户端
     */
    private List<String> ignoreClients;

    /**
     * 不需要密码解密的客户端
     */
    private List<String> ignoreDecodeClients;

    /**
     * 网关支持的语言（国际化）类型
     */
    private List<String> languages;
}
