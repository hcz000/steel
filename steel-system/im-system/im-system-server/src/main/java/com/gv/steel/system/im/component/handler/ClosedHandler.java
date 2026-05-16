package com.gv.steel.system.im.component.handler;

import com.farsunset.cim.handler.CIMRequestHandler;
import com.farsunset.cim.model.SentBody;
import com.gv.steel.system.im.component.handler.annotation.CIMHandler;
import com.gv.steel.system.im.constants.Constants;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * 连接断开时，更新用户相关状态
 */
@Slf4j
@CIMHandler(key = "client_closed")
public class ClosedHandler implements CIMRequestHandler {

    @Override
    public void process(Channel channel, SentBody message) {

        Long sessionId = channel.attr(Constants.SESSION_ID).get();

        log.info("session-id[{}] 链接关闭", sessionId);
    }

}
