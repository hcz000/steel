package com.gv.steel.system.im.component.message.rocketmq;

import com.farsunset.cim.constant.ChannelAttr;
import com.farsunset.cim.group.SessionGroup;
import com.farsunset.cim.model.Message;
import com.gv.steel.common.rocketmq.base.BaseMessage;
import com.gv.steel.common.rocketmq.core.RocketMQEnhanceTemplate;
import com.gv.steel.common.rocketmq.handler.EnhanceMessageHandler;
import com.gv.steel.system.im.config.properties.CIMProperties;
import com.gv.steel.system.im.constants.Constants;
import com.gv.steel.system.im.entity.Session;
import io.netty.channel.Channel;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@RocketMQMessageListener(
        consumerGroup = Constants.IM_BIND_CONSUMER_GROUP,
        topic = Constants.IM_BIND_MSG_TOPIC,
        messageModel = MessageModel.BROADCASTING,
        consumeThreadNumber = 5
)
@ConditionalOnProperty(prefix = CIMProperties.PREFIX, name = "consumer-type", havingValue = Constants.ROCKETMQ_CONSUMER)
public class RocketMQBindMessageListener extends EnhanceMessageHandler<Session, BaseMessage<Session>> implements RocketMQListener<BaseMessage<Session>> {

    public RocketMQBindMessageListener(RocketMQEnhanceTemplate rocketMQEnhanceTemplate, SessionGroup sessionGroup) {
        super(rocketMQEnhanceTemplate);
        conflictMap.put(Session.CHANNEL_ANDROID, new String[]{Session.CHANNEL_ANDROID, Session.CHANNEL_IOS});
        conflictMap.put(Session.CHANNEL_IOS, new String[]{Session.CHANNEL_ANDROID, Session.CHANNEL_IOS});
        conflictMap.put(Session.CHANNEL_WINDOWS, new String[]{Session.CHANNEL_WINDOWS, Session.CHANNEL_WEB, Session.CHANNEL_MAC});
        conflictMap.put(Session.CHANNEL_WEB, new String[]{Session.CHANNEL_WINDOWS, Session.CHANNEL_WEB, Session.CHANNEL_MAC});
        conflictMap.put(Session.CHANNEL_MAC, new String[]{Session.CHANNEL_WINDOWS, Session.CHANNEL_WEB, Session.CHANNEL_MAC});
        this.sessionGroup = sessionGroup;
    }

    private static final String FORCE_OFFLINE_ACTION = "999";

    private static final String SYSTEM_ID = "0";

    /*
     一个账号只能在同一个类型的终端登录
     如: 多个android或ios不能同时在线
         一个android或ios可以和web，桌面同时在线
     */
    private final Map<String, String[]> conflictMap = new HashMap<>();

    private final SessionGroup sessionGroup;

    @Override
    public void onMessage(BaseMessage<Session> message) {
        dispatchMessage(message);
    }

    @Override
    protected void handleMessage(BaseMessage<Session> message) throws Exception {
        handle(message.getBody());
    }

    private void handle(Session session) {

        String uid = session.getUid();

        String[] conflictChannels = conflictMap.get(session.getChannel());

        if (ArrayUtils.isEmpty(conflictChannels)) {
            return;
        }

        Collection<Channel> channelList = sessionGroup.find(uid, conflictChannels);

        channelList.removeIf(channel -> session.getNid().equals(channel.attr(ChannelAttr.ID).get()));

        /*
         * 获取到其他在线的终端连接，提示账号再其他终端登录
         */
        channelList.forEach(channel -> {

            if (Objects.equals(session.getDeviceId(), channel.attr(ChannelAttr.DEVICE_ID).get())) {
                channel.close();
                return;
            }

            Message message = new Message();
            message.setAction(FORCE_OFFLINE_ACTION);
            message.setReceiver(uid);
            message.setSender(SYSTEM_ID);
            message.setContent(session.getDeviceName());
            channel.writeAndFlush(message);
            channel.close();
        });

    }
}
