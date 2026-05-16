package com.gv.steel.common.redis.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(
        prefix = "garden.cache-manager"
)
public class CacheManagerProperties {
    private List<CacheConfig> configs;

    @Data
    public static class CacheConfig {
        private String key;
        private long second = 60L;
    }
}
