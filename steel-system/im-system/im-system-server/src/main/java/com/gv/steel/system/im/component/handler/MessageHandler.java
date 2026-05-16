package com.gv.steel.system.im.component.handler;

import com.farsunset.cim.handler.CIMRequestHandler;
import com.farsunset.cim.model.ReplyBody;
import com.farsunset.cim.model.SentBody;
import com.gv.steel.system.im.component.handler.annotation.CIMHandler;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@CIMHandler(key = "send_message")
public class MessageHandler implements CIMRequestHandler {
    @Override
    public void process(Channel channel, SentBody body) {
        log.info("get message: {}", body.getData());

        ReplyBody reply = new ReplyBody();
        reply.setKey(body.getKey());
        reply.setCode(HttpStatus.OK.value());
        reply.setTimestamp(System.currentTimeMillis());
        reply.getData().put("msgId", body.getData().get("msgId"));

        /*
         *向客户端发送bind响应
         */
        channel.writeAndFlush(reply);
    }
}
