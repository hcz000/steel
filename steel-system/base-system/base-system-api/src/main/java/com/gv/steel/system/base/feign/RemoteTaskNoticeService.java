package com.gv.steel.system.base.feign;

import com.gv.steel.common.core.api.Result;
import com.gv.steel.system.base.constant.BaseAppConstant;
import com.gv.steel.system.base.entity.TaskNotice;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "remoteTaskNoticeService", name = BaseAppConstant.APP_NAME)
public interface RemoteTaskNoticeService {

    @GetMapping("/task-notice/getTodoList")
    Result<List<TaskNotice>> getTodoList(@RequestParam("tableNames") String tableNames);
}
