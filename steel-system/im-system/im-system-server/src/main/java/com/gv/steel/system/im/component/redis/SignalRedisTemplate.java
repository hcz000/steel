package com.gv.steel.system.im.component.redis;

import com.alibaba.fastjson.JSONObject;
import com.farsunset.cim.model.Message;
import com.gv.steel.common.redis.utils.RedisUtil;
import com.gv.steel.system.im.component.event.MessageEvent;
import com.gv.steel.system.im.component.event.SessionEvent;
import com.gv.steel.system.im.constants.Constants;
import com.gv.steel.system.im.entity.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SignalRedisTemplate {

    @Resource
    private ApplicationContext applicationContext;

    /**
     * 消息发送到 集群中的每个实例，获取对应长连接进行消息写入
     *
     * @param message
     */
    public void push(Message message) {
        if (isDev()) {
            applicationContext.publishEvent(new MessageEvent(message));
            return;
        }
        RedisUtil.publish(Constants.PUSH_MESSAGE_INNER_QUEUE, JSONObject.toJSONString(message));
    }

    /**
     * 消息发送到 集群中的每个实例，解决多终端在线冲突问题
     *
     * @param session
     */
    public void bind(Session session) {
        if (isDev()) {
            applicationContext.publishEvent(new SessionEvent(session));
            return;
        }
        RedisUtil.publish(Constants.BIND_MESSAGE_INNER_QUEUE, JSONObject.toJSONString(session));
    }

    /**
     * 本地调试环境下不走redis，避免lettuce 经常command timeout。
     *
     * @return
     */
    private boolean isDev() {
        return false;
    }

}
