package com.gv.steel.common.rocketmq.core;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.gv.steel.common.rocketmq.base.BaseMessage;
import com.gv.steel.common.rocketmq.enums.DelayLevelEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

/**
 * RocketMQ Template 增强类
 */
@Getter
@Slf4j
@RequiredArgsConstructor
public class RocketMQEnhanceTemplate {
    private final static long DEFAULT_SEND_MESSAGE_TIME_OUT = 3000;

    private final RocketMQTemplate rocketMQTemplate;

    public String buildDestination(String topic, String tag) {
        return StrUtil.join(":", topic, tag);
    }

    /**
     * 同步发送消息
     *
     * @param topic   主题
     * @param tag     标签
     * @param message 消息体
     * @param <T>     extend BaseMessage
     * @return 发送结果
     */
    public <E, T extends BaseMessage<E>> SendResult send(String topic, String tag, T message) {
        return send(buildDestination(topic, tag), message);
    }

    /**
     * 同步发送消息
     *
     * @param destination topic:tag
     * @param message     消息体
     * @param <T>         extend BaseMessage
     * @return 发送结果
     */
    public <E, T extends BaseMessage<E>> SendResult send(String destination, T message) {
        message.setKey(UUID.fastUUID().toString(true));

        Message<T> sendMessage = MessageBuilder.withPayload(message).setHeader(RocketMQHeaders.KEYS, message.getKey()).build();

        SendResult sendResult = rocketMQTemplate.syncSend(destination, sendMessage);

        log.info("[{}]同步消息[{}]发送结果为[{}], body: [{}]", destination, message.getKey(), sendResult.getSendStatus(), message);

        return sendResult;
    }

    /**
     * 发送延迟消息
     *
     * @param topic      主题
     * @param tag        标签
     * @param message    消息体
     * @param delayLevel 延迟登记
     * @param <T>        extend BaseMessage
     * @return 发送结果
     */
    public <E, T extends BaseMessage<E>> SendResult send(String topic, String tag, T message, int delayLevel) {
        return send(buildDestination(topic, tag), message, delayLevel);
    }

    public <E, T extends BaseMessage<E>> SendResult send(String destination, T message, int delayLevel) {
        message.setKey(UUID.fastUUID().toString(true));

        Message<T> sendMessage = MessageBuilder.withPayload(message).setHeader(RocketMQHeaders.KEYS, message.getKey()).build();

        SendResult sendResult = rocketMQTemplate.syncSend(destination, sendMessage, DEFAULT_SEND_MESSAGE_TIME_OUT, delayLevel);

        log.info("[{}]延迟等级[{}]同步消息[{}]发送结果为[{}], body: [{}]", destination, delayLevel, message.getKey(), sendResult.getSendStatus(), message);

        return sendResult;
    }

    public <E, T extends BaseMessage<E>> void asyncSend(String topic, String tag, T message) {
        asyncSend(topic, tag, message, DelayLevelEnum.NO_DELAY.getLevel());
    }

    public <E, T extends BaseMessage<E>> void asyncSend(String topic, String tag, T message, int delayLevel) {
        String destination = buildDestination(topic, tag);
        asyncSend(destination, message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("[{}]延迟等级[{}]异步消息[{}]发送成功, body: [{}]", destination, delayLevel, message.getKey(), message);
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("[{}]延迟等级[{}]异步消息[{}]发送失败, body: [{}]，error: {}", destination, delayLevel, message.getKey(), message, throwable.getLocalizedMessage());
            }
        }, delayLevel);
    }

    public <E, T extends BaseMessage<E>> void asyncSend(String topic, String tag, T message, SendCallback sendCallback) {
        asyncSend(buildDestination(topic, tag), message, sendCallback, DelayLevelEnum.NO_DELAY.getLevel());
    }

    public <E, T extends BaseMessage<E>> void asyncSend(String destination, T message, SendCallback sendCallback, int delayLevel) {
        message.setKey(UUID.fastUUID().toString(true));

        Message<T> sendMessage = MessageBuilder.withPayload(message).setHeader(RocketMQHeaders.KEYS, message.getKey()).build();

        rocketMQTemplate.asyncSend(destination, sendMessage, sendCallback, DEFAULT_SEND_MESSAGE_TIME_OUT, delayLevel);
    }
}
