package com.gv.steel.system.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.easyexcel.annotation.EasyExcelExport;
import com.gv.steel.common.security.annotation.Inner;
import com.gv.steel.system.user.dto.SysLogDTO;
import com.gv.steel.system.user.entity.SysLog;
import com.gv.steel.system.user.service.SysLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 日志表 前端控制器
 * </p>
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/log")
@Tag(name = "日志管理模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysLogController {

    private final SysLogService sysLogService;

    /**
     * 简单分页查询
     *
     * @param page   分页对象
     * @param sysLog 系统日志
     * @return
     */
    @Operation(summary = "简单分页查询", description = "简单分页查询")
    @Parameters({
            @Parameter(name = "current", description = "当前页"),
            @Parameter(name = "size", description = "每页显示条数"),
            @Parameter(name = "type", description = "日志类型"),
            @Parameter(name = "createTime", description = "创建时间区间 [开始时间，结束时间]"),
            @Parameter(name = "remoteAddr", description = "IP地址"),
    })
    @GetMapping("/page")
    public Result<PageResult<SysLog>> getLogPage(@Parameter(hidden = true) Page<SysLog> page, @Parameter(hidden = true) SysLogDTO sysLog) {
        return Result.ok(PageResult.<SysLog>builder().build().pageResult(sysLogService.getLogByPage(page, sysLog)));
    }

    /**
     * 删除日志
     *
     * @param id ID
     * @return success/false
     */
    @Operation(summary = "删除日志", description = "删除日志")
    @Parameter(name = "id", description = "日志ID")
    @DeleteMapping("/{id:\\d+}")
    @PreAuthorize("@pms.hasPermission('sys_log_del')")
    public Result<Boolean> removeById(@PathVariable Long id) {
        return Result.ok(sysLogService.removeById(id));
    }

    /**
     * 插入日志
     *
     * @param sysLog 日志实体
     * @return success/false
     */
    @Operation(summary = "插入日志", description = "插入日志")
    @Inner
    @PostMapping
    public Result<Boolean> save(@Valid @RequestBody SysLog sysLog) {
        return Result.ok(sysLogService.save(sysLog));
    }

    /**
     * 导出excel 表格
     *
     * @param sysLog 查询条件
     * @return EXCEL
     */
    @EasyExcelExport(name = "日志文件")
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('sys_log_import_export')")
    public List<SysLog> export(SysLogDTO sysLog) {
        return sysLogService.getLogList(sysLog);
    }

}
