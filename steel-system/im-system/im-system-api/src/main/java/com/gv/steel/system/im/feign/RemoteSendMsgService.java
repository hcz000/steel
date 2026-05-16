package com.gv.steel.system.im.feign;

import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.core.constant.ServiceNameConstants;
import com.gv.steel.system.im.dto.MessageDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(contextId = "remoteSendMsgService", value = ServiceNameConstants.IM_SYSTEM_SERVER)
public interface RemoteSendMsgService {
    @PostMapping(value = "/im/send")
    Result<Long> send(@RequestBody MessageDTO msg);

    @PostMapping(value = "/im/batch-send")
    Result<Boolean> send(@RequestBody List<MessageDTO> msgList);
}
