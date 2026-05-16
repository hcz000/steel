package com.gv.steel.system.im.component.push;

import com.farsunset.cim.model.Message;
import com.gv.steel.common.rocketmq.base.BaseMessage;
import com.gv.steel.common.rocketmq.constant.RocketMQConstant;
import com.gv.steel.common.rocketmq.core.RocketMQEnhanceTemplate;
import com.gv.steel.system.im.config.properties.CIMProperties;
import com.gv.steel.system.im.constants.Constants;
import com.gv.steel.system.im.entity.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/*
 * 消息发送实现类
 *
 */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = CIMProperties.PREFIX, name = "consumer-type", havingValue = Constants.ROCKETMQ_CONSUMER)
public class RocketMQMessagePusher implements CIMMessagePusher {
    private final RocketMQEnhanceTemplate rocketMQEnhanceTemplate;

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
        BaseMessage<Message> bMsg = new BaseMessage<>();
        bMsg.setBody(message);
        rocketMQEnhanceTemplate.asyncSend(Constants.IM_PUSH_MSG_TOPIC, RocketMQConstant.DEFAULT_TAG, bMsg);
    }

    @Override
    public void bind(Session session) {
        BaseMessage<Session> bMsg = new BaseMessage<>();
        bMsg.setBody(session);
        rocketMQEnhanceTemplate.asyncSend(Constants.IM_BIND_MSG_TOPIC, RocketMQConstant.DEFAULT_TAG, bMsg);
    }
}
