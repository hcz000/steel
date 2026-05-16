package com.gv.steel.system.im.component.message.redis;

import com.alibaba.fastjson.JSONObject;
import com.farsunset.cim.constant.ChannelAttr;
import com.farsunset.cim.group.SessionGroup;
import com.farsunset.cim.model.Message;
import com.gv.steel.system.im.component.event.SessionEvent;
import com.gv.steel.system.im.config.properties.CIMProperties;
import com.gv.steel.system.im.constants.Constants;
import com.gv.steel.system.im.entity.Session;
import io.netty.channel.Channel;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 集群环境下，监控多设备登录情况，控制是否其余终端下线的逻辑
 */
@Component
@ConditionalOnProperty(prefix = CIMProperties.PREFIX, name = "consumer-type", havingValue = Constants.REDIS_CONSUMER, matchIfMissing = true)
public class RedisBindMessageListener implements Consumer<String> {

    private static final String FORCE_OFFLINE_ACTION = "999";

    private static final String SYSTEM_ID = "0";

    /**
     * 一个账号只能在同一个类型的终端登录
     * 如: 多个android或ios不能同时在线
     * 一个android或ios可以和web，桌面同时在线
     */
    private final Map<String, String[]> conflictMap = new HashMap<>();

    private final SessionGroup sessionGroup;

    public RedisBindMessageListener(SessionGroup sessionGroup) {
        conflictMap.put(Session.CHANNEL_ANDROID, new String[]{Session.CHANNEL_ANDROID, Session.CHANNEL_IOS});
        conflictMap.put(Session.CHANNEL_IOS, new String[]{Session.CHANNEL_ANDROID, Session.CHANNEL_IOS});
        conflictMap.put(Session.CHANNEL_WINDOWS, new String[]{Session.CHANNEL_WINDOWS, Session.CHANNEL_WEB, Session.CHANNEL_MAC});
        conflictMap.put(Session.CHANNEL_WEB, new String[]{Session.CHANNEL_WINDOWS, Session.CHANNEL_WEB, Session.CHANNEL_MAC});
        conflictMap.put(Session.CHANNEL_MAC, new String[]{Session.CHANNEL_WINDOWS, Session.CHANNEL_WEB, Session.CHANNEL_MAC});

        this.sessionGroup = sessionGroup;
    }

    @EventListener
    public void onMessage(SessionEvent event) {
        this.handle(event.getSource());
    }

    @Override
    public void accept(String body) {
        Session session = JSONObject.parseObject(body, Session.class);
        this.handle(session);
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
