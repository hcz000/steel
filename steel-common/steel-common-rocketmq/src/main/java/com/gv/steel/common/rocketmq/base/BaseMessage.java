package com.gv.steel.common.rocketmq.base;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消息实体，统一消息格式
 */
@Data
public class BaseMessage<E> implements Serializable {
    private static final long serialVersionUID = 1143521274096008265L;

    public BaseMessage(E body) {
        this.body = body;
    }

    public BaseMessage() {
    }

    /**
     * 业务key，用于MQ查看消息情况，默认UUID
     */
    private String key;
    /**
     * 消息来源，用于排查
     */
    private String source = "";
    /**
     * 发送时间
     */
    private LocalDateTime sendTime = LocalDateTime.now();
    /**
     * 重试次数，用于判断重试次数，超过重试次数发送异常警告
     */
    private Integer retryTimes = 0;
    /**
     * 消息体
     */
    private E body;
}
