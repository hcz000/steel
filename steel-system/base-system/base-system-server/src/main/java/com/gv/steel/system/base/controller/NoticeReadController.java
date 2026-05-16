package com.gv.steel.system.base.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.system.base.entity.NoticeRead;
import com.gv.steel.system.base.service.NoticeReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 通知公告阅读情况表 前端控制器
 * </p>
 *
 * @author administrator
 * @since 2023-11-13
 */
@Tag(name = "通知公告阅读情况表接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/notice-read")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class NoticeReadController {

    private final NoticeReadService noticeReadService;

    /**
     * 通知公告阅读情况表列表
     */
    @Operation(summary = "通知公告阅读情况表查询列表", hidden = true)
    @Parameters({
            @Parameter(name = "current", description = "当前页"),
            @Parameter(name = "size", description = "每页显示条数")
    })
    @GetMapping("/page")
    public Result<PageResult<NoticeRead>> getPage(@Parameter(hidden = true) Page<NoticeRead> page) {
        return Result.ok(PageResult.<NoticeRead>builder().build().pageResult(noticeReadService.page(page)));
    }

    /**
     * 通知公告阅读情况表查询
     */
    @Operation(summary = "通知公告阅读情况表查询", hidden = true)
    @Parameter(name = "id", description = "通知公告阅读情况表ID", required = true)
    @GetMapping("/{id:\\d+}")
    public Result<NoticeRead> findNoticeReadById(@PathVariable Long id) {
        return Result.ok(noticeReadService.getById(id));
    }

    /**
     * 通知公告阅读情况表新增
     */
    @Operation(summary = "通知公告阅读情况表新增", hidden = true)
    @PostMapping
    public Result<Boolean> save(@RequestBody @Valid NoticeRead noticeRead) {
        return Result.ok(noticeReadService.save(noticeRead));
    }

    /**
     * 通知公告阅读情况表修改
     */
    @Operation(summary = "通知公告阅读情况表修改", hidden = true)
    @PutMapping
    public Result<Boolean> update(@RequestBody @Valid NoticeRead noticeRead) {
        return Result.ok(noticeReadService.updateById(noticeRead));
    }

    /**
     * 通知公告阅读情况表删除
     */
    @Operation(summary = "通知公告阅读情况表删除", hidden = true)
    @Parameter(name = "id", description = "通知公告阅读情况表ID", required = true)
    @DeleteMapping("/{id:\\d+}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.ok(noticeReadService.removeById(id));
    }
}
