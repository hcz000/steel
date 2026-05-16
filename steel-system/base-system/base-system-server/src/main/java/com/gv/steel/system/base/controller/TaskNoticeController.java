package com.gv.steel.system.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.core.context.UserInfoContextHolder;
import com.gv.steel.common.mybatis.utils.QueryWrapperUtil;
import com.gv.steel.common.security.annotation.Inner;
import com.gv.steel.system.base.dto.ReceiveTaskNoticeQueryDTO;
import com.gv.steel.system.base.dto.SendTaskNoticeQueryDTO;
import com.gv.steel.system.base.entity.TaskNotice;
import com.gv.steel.system.base.enums.TaskNoticeStatusEnum;
import com.gv.steel.system.base.service.TaskNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 任务通知表(收发件信箱) 前端控制器
 * </p>
 *
 * @author administrator
 * @since 2023-11-13
 */
@Tag(name = "任务通知表(收发件信箱)接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/task-notice")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class TaskNoticeController {

    private final TaskNoticeService taskNoticeService;

    /**
     * 任务通知表(收发件信箱)列表
     */
    @Operation(summary = "任务通知表(收件信箱)查询列表")
    @Parameters({
            @Parameter(name = "current", description = "当前页"),
            @Parameter(name = "size", description = "每页显示条数"),
            @Parameter(name = "title", description = "待办标题"),
            @Parameter(name = "createTime", description = "任务创建时间，format: yyyy-MM-dd,yyyy-MM-dd"),
            @Parameter(name = "status", description = "任务状态，0: 待办，1：已审批，2：审批不通过"),
            @Parameter(name = "createName", description = "发起人名称，模糊查询")
    })
    @GetMapping("/receive/page")
    public Result<PageResult<TaskNotice>> getReceivePage(@Parameter(hidden = true) Page<TaskNotice> page, @Parameter(hidden = true) ReceiveTaskNoticeQueryDTO dto) {
        dto.setUserId(UserInfoContextHolder.currentUserId());
        QueryWrapper<TaskNotice> wrapper = QueryWrapperUtil.queryWrapperHandler(dto);
        return Result.ok(PageResult.<TaskNotice>builder().build().pageResult(taskNoticeService.page(page, wrapper)));
    }

    /**
     * 任务通知待办数
     */
    @Operation(summary = "任务通知待办数")
    @GetMapping("/receive/count")
    public Result<Long> getReceiveCount() {
        ReceiveTaskNoticeQueryDTO dto = new ReceiveTaskNoticeQueryDTO();
        dto.setUserId(UserInfoContextHolder.currentUserId());
        dto.setStatus(Lists.newArrayList(TaskNoticeStatusEnum.PENDING.getStatus()));
        QueryWrapper<TaskNotice> wrapper = QueryWrapperUtil.queryWrapperHandler(dto);
        return Result.ok(taskNoticeService.count(wrapper));
    }

    /**
     * 任务通知表(收发件信箱)列表
     */
    @Operation(summary = "任务通知表(发件信箱)查询列表")
    @Parameters({
            @Parameter(name = "current", description = "当前页"),
            @Parameter(name = "size", description = "每页显示条数"),
            @Parameter(name = "title", description = "待办标题"),
            @Parameter(name = "createTime", description = "任务创建时间，format: yyyy-MM-dd,yyyy-MM-dd"),
            @Parameter(name = "status", description = "任务状态，0: 待办，1：已审批，2：审批不通过"),
            @Parameter(name = "receiveName", description = "接收人名称，模糊查询")
    })
    @GetMapping("/send/page")
    public Result<PageResult<TaskNotice>> getSendPage(@Parameter(hidden = true) Page<TaskNotice> page, @Parameter(hidden = true) SendTaskNoticeQueryDTO dto) {
        dto.setUserId(UserInfoContextHolder.currentUserId());
        QueryWrapper<TaskNotice> wrapper = QueryWrapperUtil.queryWrapperHandler(dto);
        return Result.ok(PageResult.<TaskNotice>builder().build().pageResult(taskNoticeService.page(page, wrapper)));
    }

    /**
     * 任务通知表(收发件信箱)查询
     */
    @Operation(summary = "任务通知表(收发件信箱)查询")
    @Parameter(name = "id", description = "任务通知表(收发件信箱)ID", required = true)
    @GetMapping("/{id:\\d+}")
    public Result<TaskNotice> findTaskNoticeById(@PathVariable Long id) {
        return Result.ok(taskNoticeService.getById(id));
    }

    @Inner(false)
    @Operation(summary = "通过业务表名集合，获取待办的任务列表", hidden = true)
    @Parameter(name = "tableNames", description = "业务表名集合")
    @GetMapping("/getTodoList")
    public Result<List<TaskNotice>> getTodoList(@RequestParam List<String> tableNames) {
        return Result.ok(
                taskNoticeService.list(
                        Wrappers.<TaskNotice>lambdaQuery()
                                .in(TaskNotice::getTableName, tableNames)
                                .eq(TaskNotice::getStatus, TaskNoticeStatusEnum.PENDING.getStatus())
                )
        );
    }
}
