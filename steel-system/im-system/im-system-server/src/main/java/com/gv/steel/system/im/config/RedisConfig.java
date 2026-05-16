package com.gv.steel.system.im.config;

import com.gv.steel.system.im.config.properties.CIMProperties;
import com.gv.steel.system.im.constants.Constants;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;


@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = CIMProperties.PREFIX, name = "consumer-type", havingValue = Constants.REDIS_CONSUMER, matchIfMissing = true)
public class RedisConfig {

    private final Consumer<String> redisPushMessageListener;

    private final Consumer<String> redisBindMessageListener;

    private final RedissonClient redissonClient;

    @PostConstruct
    public void redisMessageListenerContainer() {
        RTopic topic = redissonClient.getTopic(Constants.PUSH_MESSAGE_INNER_QUEUE);
        topic.addListener(String.class, (channel, msg) -> {
            redisPushMessageListener.accept(msg);
        });
        RTopic topic1 = redissonClient.getTopic(Constants.BIND_MESSAGE_INNER_QUEUE);
        topic1.addListener(String.class, (channel, msg) -> {
            redisBindMessageListener.accept(msg);
        });
//        RedisUtil.subscribe(Constants.PUSH_MESSAGE_INNER_QUEUE, String.class, pushMessageListener);
//        RedisUtil.subscribe(Constants.BIND_MESSAGE_INNER_QUEUE, String.class, bindMessageListener);
    }

}