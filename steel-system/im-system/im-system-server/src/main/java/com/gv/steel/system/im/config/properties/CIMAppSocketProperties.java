package com.gv.steel.system.im.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "cim.app")
public class CIMAppSocketProperties {

    private boolean enable;

    private Integer port;

    /**
     * 长链接写空闲时间触发时间(s)
     * 心跳发送定时，每当x秒无数据下发写入，触发 服务端-->客户端 心跳事件
     */
    private Duration writeIdle = Duration.ofSeconds(45);

    /**
     * 长链接读空闲时间触发时间(s)
     * 心跳响应定时，每当readIdle - writeIdle秒无数据接收，触发心跳超时计数
     */
    private Duration readIdle = Duration.ofSeconds(60);

    /**
     * 长链接最大允许心跳响应超时次数
     * 达到该次数则 服务端断开链接
     */
    private int maxPongTimeout = 1;
}
