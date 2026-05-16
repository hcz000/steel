package com.gv.steel.system.im.component.message.redis;

import com.alibaba.fastjson.JSONObject;
import com.farsunset.cim.group.SessionGroup;
import com.farsunset.cim.model.Message;
import com.gv.steel.system.im.component.event.MessageEvent;
import com.gv.steel.system.im.config.properties.CIMProperties;
import com.gv.steel.system.im.constants.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;


/**
 * 集群环境下，监听redis队列，广播消息到每个实例进行推送
 * 如果使用MQ的情况也，最好替换为MQ消息队列
 */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = CIMProperties.PREFIX, name = "consumer-type", havingValue = Constants.REDIS_CONSUMER, matchIfMissing = true)
public class RedisPushMessageListener implements Consumer<String> {
    private final SessionGroup sessionGroup;

    @Override
    public void accept(String body) {
        Message message = JSONObject.parseObject(body, Message.class);

        this.onMessage(message);
    }

    @EventListener
    public void onMessage(MessageEvent event) {
        this.onMessage(event.getSource());
    }

    public void onMessage(Message message) {

        String uid = message.getReceiver();

        if (uid == null) {
            return;
        }

        sessionGroup.write(uid, message);
    }
}
