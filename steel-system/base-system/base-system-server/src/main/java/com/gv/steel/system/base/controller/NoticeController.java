package com.gv.steel.system.base.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.log.annotation.SysLog;
import com.gv.steel.system.base.dto.NoticeQueryDTO;
import com.gv.steel.system.base.entity.Notice;
import com.gv.steel.system.base.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 通知公告 前端控制器
 * </p>
 *
 * @author administrator
 * @since 2023-11-13
 */
@Tag(name = "通知公告接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class NoticeController {

    private final NoticeService noticeService;

    /**
     * 通知公告列表
     */
    @Operation(summary = "通知公告查询列表")
    @Parameters({
            @Parameter(name = "current", description = "当前页"),
            @Parameter(name = "size", description = "每页显示条数"),
            @Parameter(name = "title", description = "公告标题"),
            @Parameter(name = "publishStatus", description = "发布状态，0否，1是")
    })
    @GetMapping("/page")
    public Result<PageResult<Notice>> getPage(@Parameter(hidden = true) Page<Notice> page,
                                              @Parameter(hidden = true) NoticeQueryDTO dto) {
        return Result.ok(noticeService.getPage(page, dto));
    }

    /**
     * 通知公告查询
     */
    @Operation(summary = "通知公告查询")
    @Parameter(name = "id", description = "通知公告ID", required = true)
    @GetMapping("/{id:\\d+}")
    public Result<Notice> findNoticeById(@PathVariable Long id) {
        return Result.ok(noticeService.getDetailById(id));
    }

    /**
     * 通知公告新增
     */
    @Operation(summary = "通知公告新增")
    @SysLog("新增通知公告")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('notice_edit')")
    public Result<Boolean> save(@RequestBody @Valid Notice notice) {
        return Result.ok(noticeService.save(notice));
    }

    /**
     * 通知公告修改
     */
    @SysLog("修改通知公告")
    @Operation(summary = "通知公告修改")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('notice_edit')")
    public Result<Boolean> update(@RequestBody @Valid Notice notice) {
        return Result.ok(noticeService.updateById(notice));
    }

    /**
     * 通知公告删除
     */
    @SysLog("删除通知公告")
    @Operation(summary = "通知公告删除")
    @Parameter(name = "id", description = "通知公告ID", required = true)
    @DeleteMapping("/{id:\\d+}")
    @PreAuthorize("@pms.hasPermission('notice_edit')")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.ok(noticeService.removeById(id));
    }
}
