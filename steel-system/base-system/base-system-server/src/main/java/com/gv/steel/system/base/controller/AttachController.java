package com.gv.steel.system.base.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.system.base.entity.Attach;
import com.gv.steel.system.base.service.AttachService;
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
 * 附件表 前端控制器
 * </p>
 *
 * @author administrator
 * @since 2023-11-13
 */
@Tag(name = "附件表接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/attach")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class AttachController {

    private final AttachService attachService;

    /**
     * 附件表列表
     */
    @Operation(summary = "附件表查询列表", hidden = true)
    @Parameters({
            @Parameter(name = "current", description = "当前页"),
            @Parameter(name = "size", description = "每页显示条数")
    })
    @GetMapping("/page")
    public Result<PageResult<Attach>> getPage(@Parameter(hidden = true) Page<Attach> page) {
        return Result.ok(PageResult.<Attach>builder().build().pageResult(attachService.page(page)));
    }

    /**
     * 附件表查询
     */
    @Operation(summary = "附件表查询", hidden = true)
    @Parameter(name = "id", description = "附件表ID", required = true)
    @GetMapping("/{id:\\d+}")
    public Result<Attach> findAttachById(@PathVariable Long id) {
        return Result.ok(attachService.getById(id));
    }

    /**
     * 附件表新增
     */
    @Operation(summary = "附件表新增", hidden = true)
    @PostMapping
    public Result<Boolean> save(@RequestBody @Valid Attach attach) {
        return Result.ok(attachService.save(attach));
    }

    /**
     * 附件表修改
     */
    @Operation(summary = "附件表修改", hidden = true)
    @PutMapping
    public Result<Boolean> update(@RequestBody @Valid Attach attach) {
        return Result.ok(attachService.updateById(attach));
    }

    /**
     * 附件表删除
     */
    @Operation(summary = "附件表删除", hidden = true)
    @Parameter(name = "id", description = "附件表ID", required = true)
    @DeleteMapping("/{id:\\d+}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.ok(attachService.removeById(id));
    }
}
