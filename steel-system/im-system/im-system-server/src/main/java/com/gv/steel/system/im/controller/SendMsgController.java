package com.gv.steel.system.im.controller;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.farsunset.cim.model.Message;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.system.im.component.push.CIMMessagePusher;
import com.gv.steel.system.im.convert.MessageMapper;
import com.gv.steel.system.im.dto.MessageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/im")
@Tag(name = "websocket消息推送接")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SendMsgController {
    private final CIMMessagePusher cimMessagePusher;
    private final MessageMapper messageMapper;

    @Operation(summary = "发送单条消息")
    @PostMapping(value = "/send")
    public Result<Long> send(@RequestBody MessageDTO msg) {
        Message message = messageMapper.convert(msg);

        message.setId(new SnowflakeGenerator().next());

        cimMessagePusher.push(message);

        return Result.ok(message.getId());
    }

    @Operation(summary = "批量发送消息")
    @PostMapping(value = "/batch-send")
    public Result<Boolean> send(@RequestBody List<MessageDTO> msgList) {
        List<Message> messageList = messageMapper.convertList(msgList);

        messageList.forEach(msg -> {
            msg.setId(new SnowflakeGenerator().next());
            cimMessagePusher.push(msg);
        });

        return Result.ok();
    }
}
