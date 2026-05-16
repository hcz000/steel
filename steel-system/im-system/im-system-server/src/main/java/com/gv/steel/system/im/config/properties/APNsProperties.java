package com.gv.steel.system.im.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cim.apns")
public class APNsProperties {

    private boolean debug;

    private String appId;

    private final P12 p12 = new P12();

    @Data
    public static class P12 {
        private String file;
        private String password;
    }
}
