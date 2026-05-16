package com.gv.steel.system.im.component.handler;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import cn.hutool.core.util.StrUtil;
import com.farsunset.cim.constant.ChannelAttr;
import com.farsunset.cim.group.SessionGroup;
import com.farsunset.cim.handler.CIMRequestHandler;
import com.farsunset.cim.model.ReplyBody;
import com.farsunset.cim.model.SentBody;
import com.gv.steel.system.im.component.handler.annotation.CIMHandler;
import com.gv.steel.system.im.component.push.CIMMessagePusher;
import com.gv.steel.system.im.constants.Constants;
import com.gv.steel.system.im.entity.Session;
import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;

/**
 * 客户长连接 账户绑定实现
 */
@Slf4j
@RequiredArgsConstructor
@CIMHandler(key = "client_bind")
public class BindHandler implements CIMRequestHandler {
    private static final String AUTHORIZATION = "token";

    private static final String CLIENT_HANDSHAKE = "client_handshake";

    private final SessionGroup sessionGroup;

    private final CIMMessagePusher cimMessagePusher;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void process(Channel channel, SentBody body) {

        if (sessionGroup.isManaged(channel)) {
            return;
        }

        String uid = body.get("uid");
        String token = body.get("token");
        String uname = body.get("username");

        Session session = new Session();
        session.setId(new SnowflakeGenerator().next());
        session.setUid(uid);
        session.setUname(uname);
        session.setNid(channel.attr(ChannelAttr.ID).get());
        session.setDeviceId(body.get("deviceId"));
        session.setChannel(body.get("channel"));
        session.setDeviceName(body.get("deviceName"));
        session.setAppVersion(body.get("appVersion"));
        session.setOsVersion(body.get("osVersion"));
        session.setLanguage(body.get("language"));

        channel.attr(ChannelAttr.UID).set(uid);
        channel.attr(ChannelAttr.CHANNEL).set(session.getChannel());
        channel.attr(ChannelAttr.DEVICE_ID).set(session.getDeviceId());
        channel.attr(ChannelAttr.LANGUAGE).set(session.getLanguage());

        channel.attr(Constants.SESSION_ID).set(session.getId());

        // 校验token
        if (!checkToken(token, uname)) {
            ReplyBody reply = new ReplyBody();
            reply.setKey(CLIENT_HANDSHAKE);
            reply.setCode(HttpStatus.UNAUTHORIZED.value());
            reply.setTimestamp(System.currentTimeMillis());

            /*
             *向客户端发送 401 握手失败
             */
            channel.writeAndFlush(reply);
            channel.close();
            return;
        }

        ReplyBody reply = new ReplyBody();
        reply.setKey(body.getKey());
        reply.setCode(HttpStatus.OK.value());
        reply.setTimestamp(System.currentTimeMillis());

        /*
         * 添加到内存管理
         */
        sessionGroup.add(channel);

        /*
         *向客户端发送bind响应
         */
        channel.writeAndFlush(reply);

        /*
         * 发送上线事件到集群中的其他实例，控制其他设备下线
         */
        cimMessagePusher.bind(session);

        log.info("session-id[{}]链接建立成功！", session.getId());
    }

    private boolean checkToken(String token, String uname) {
        if (StrUtil.isBlank(token) || StrUtil.isBlank(uname)) {
            return false;
        }

        String tokenKey = buildKey(AUTHORIZATION, "access_token", token);
        if (Boolean.FALSE.equals(redisTemplate.hasKey(tokenKey))) {
            return false;
        }

        try {
            redisTemplate.setValueSerializer(RedisSerializer.java());
            OAuth2Authorization auth2Authorization = (OAuth2Authorization) redisTemplate.opsForValue().get(tokenKey);

            if (auth2Authorization == null) {
                return false;
            }

            return uname.equals(auth2Authorization.getPrincipalName());
        } catch (Exception e) {
            log.error("校验token异常", e);
            return false;
        }
    }

    private String buildKey(String constant, String type, String id) {
        return String.format("%s:%s:%s", constant, type, id);
    }
}
