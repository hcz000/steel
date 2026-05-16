package com.gv.steel.common.oss.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Oss配置类
 */
@Data
@ConfigurationProperties(prefix = OssProperties.prefix)
public class OssProperties {
    /**
     * 配置前缀
     */
    public final static String prefix = "oss";

    /**
     * 是否启用 oss，默认为true
     */
    private Boolean enable = true;

    /**
     * 对象存储服务的URL
     */
    private String endpoint;

    /**
     * 区域
     */
    private String region;

    /**
     * true path-style nginx 反向代理和S3默认支持 pathStyle模式 {http://endpoint/bucketname}
     * false supports virtual-hosted-style 阿里云等需要配置为 virtual-hosted-style 模式{http://bucketname.endpoint}
     * 只是url的显示不一样
     */
    private Boolean pathStyleAccess = true;

    /**
     * Access key就像用户ID，可以唯一标识你的账户
     */
    private String accessKey;

    /**
     * Secret key是你账户的密码
     */
    private String secretKey;

    /**
     * 默认的存储桶名称
     */
    private String bucketName;

    /**
     * 最大线程数，默认： 100
     */
    private Integer maxConnections = 100;

    /**
     * 允许上传的文件后缀
     */
    private String[] suffix;
}
