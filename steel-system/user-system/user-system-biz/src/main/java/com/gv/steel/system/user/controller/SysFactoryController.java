package com.gv.steel.system.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.system.user.entity.SysFactory;
import com.gv.steel.system.user.service.SysFactoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 工厂管理 前端控制器
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Tag(name = "工厂管理接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys-factory")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysFactoryController {

    private final SysFactoryService sysFactoryService;

    /**
     * 工厂管理列表
     */
    @Operation(summary = "工厂管理查询列表")
    @Parameters({
            @Parameter(name = "current", description = "当前页"),
            @Parameter(name = "size", description = "每页显示条数"),
            @Parameter(name = "factoryName", description = "工厂名称"),
            @Parameter(name = "address", description = "地址")
    })
    @GetMapping("/page")
    public Result<PageResult<SysFactory>> getPage(@Parameter(hidden = true) Page<SysFactory> page, @Parameter(hidden = true) SysFactory factory) {
        return Result.ok(PageResult.<SysFactory>builder().build().pageResult(sysFactoryService.getPage(page, factory)));
    }

    /**
     * 查询所有工厂数组
     */
    @Operation(summary = "查询所有工厂数组")
    @GetMapping("/list")
    public Result<List<SysFactory>> getList() {
        return Result.ok(sysFactoryService.list());
    }


    /**
     * 工厂管理查询
     */
    @Operation(summary = "工厂管理查询")
    @Parameter(name = "id", description = "工厂管理ID", required = true)
    @GetMapping("/{id:\\d+}")
    public Result<SysFactory> findSysFactoryById(@PathVariable Long id) {
        return Result.ok(sysFactoryService.getById(id));
    }

    /**
     * 工厂管理新增
     */
    @Operation(summary = "工厂管理新增")
    @PostMapping
    public Result<Boolean> save(@RequestBody @Valid SysFactory sysFactory) {
        return Result.ok(sysFactoryService.save(sysFactory));
    }

    /**
     * 工厂管理修改
     */
    @Operation(summary = "工厂管理修改")
    @PutMapping
    public Result<Boolean> update(@RequestBody @Valid SysFactory sysFactory) {
        return Result.ok(sysFactoryService.updateById(sysFactory));
    }

    /**
     * 工厂管理删除
     */
    @Operation(summary = "工厂管理删除")
    @Parameter(name = "id", description = "工厂管理ID", required = true)
    @DeleteMapping("/{id:\\d+}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.ok(sysFactoryService.removeById(id));
    }
}
