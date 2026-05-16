package com.gv.steel.system.im.component.push;

import com.farsunset.cim.model.Message;
import com.gv.steel.system.im.component.redis.KeyValueRedisTemplate;
import com.gv.steel.system.im.component.redis.SignalRedisTemplate;
import com.gv.steel.system.im.config.properties.CIMProperties;
import com.gv.steel.system.im.constants.Constants;
import com.gv.steel.system.im.entity.Session;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/*
 * 消息发送实现类
 *
 */
@Component
@ConditionalOnProperty(prefix = CIMProperties.PREFIX, name = "consumer-type", havingValue = Constants.REDIS_CONSUMER, matchIfMissing = true)
public class RedisMessagePusher implements CIMMessagePusher {

//    @Resource
//    private APNsService apnsService;

    @Resource
    private SignalRedisTemplate signalRedisTemplate;

    @Resource
    private KeyValueRedisTemplate keyValueRedisTemplate;

    /**
     * 向用户发送消息
     *
     * @param message
     */
    public final void push(Message message) {

//        String uid = message.getReceiver();

        /*
         * 说明iOS客户端开启了apns
         */
//        String deviceToken = keyValueRedisTemplate.getDeviceToken(uid);
//        if (deviceToken != null) {
//            apnsService.push(message, deviceToken);
//            return;
//        }

        /*
         * 通过发送redis广播，到集群中的每台实例，获得当前UID绑定了连接并推送
         * @see com.farsunset.hoxin.component.message.PushMessageListener
         */
        signalRedisTemplate.push(message);

    }

    @Override
    public void bind(Session session) {
        signalRedisTemplate.bind(session);
    }
}
