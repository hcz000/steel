package com.gv.steel.system.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.system.user.entity.SysUserFactory;
import com.gv.steel.system.user.service.SysUserFactoryService;
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
 * 用户工厂关联关系表 前端控制器
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Tag(name = "用户工厂关联关系表接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys-user-factory")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysUserFactoryController {

    private final SysUserFactoryService sysUserFactoryService;

    /**
     * 用户工厂关联关系表列表
     */
    @Operation(summary = "用户工厂关联关系表查询列表", hidden = true)
    @Parameters({
            @Parameter(name = "current", description = "当前页"),
            @Parameter(name = "size", description = "每页显示条数")
    })
    @GetMapping("/page")
    public Result<PageResult<SysUserFactory>> getPage(@Parameter(hidden = true) Page<SysUserFactory> page) {
        return Result.ok(PageResult.<SysUserFactory>builder().build().pageResult(sysUserFactoryService.page(page)));
    }

    /**
     * 用户工厂关联关系表查询
     */
    @Operation(summary = "用户工厂关联关系表查询", hidden = true)
    @Parameter(name = "id", description = "用户工厂关联关系表ID", required = true)
    @GetMapping("/{id:\\d+}")
    public Result<SysUserFactory> findSysUserFactoryById(@PathVariable Long id){
        return Result.ok(sysUserFactoryService.getById(id));
    }

    /**
     * 用户工厂关联关系表新增
     */
    @Operation(summary = "用户工厂关联关系表新增", hidden = true)
    @PostMapping
    public Result<Boolean> save(@RequestBody @Valid SysUserFactory sysUserFactory){
        return Result.ok(sysUserFactoryService.save(sysUserFactory));
    }

    /**
     * 用户工厂关联关系表修改
     */
    @Operation(summary = "用户工厂关联关系表修改", hidden = true)
    @PutMapping
    public Result<Boolean> update(@RequestBody @Valid SysUserFactory sysUserFactory){
        return Result.ok(sysUserFactoryService.updateById(sysUserFactory));
    }

    /**
     * 用户工厂关联关系表删除
     */
    @Operation(summary = "用户工厂关联关系表删除", hidden = true)
    @Parameter(name = "id", description = "用户工厂关联关系表ID", required = true)
    @DeleteMapping("/{id:\\d+}")
    public Result<Boolean> delete(@PathVariable Long id){
        return Result.ok(sysUserFactoryService.removeById(id));
        }
        }
