package com.gv.steel.system.user.controller;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.system.user.entity.SysTeam;
import com.gv.steel.system.user.service.SysTeamService;
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
 * 班组管理 前端控制器
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Tag(name = "班组管理接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys-team")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysTeamController {

    private final SysTeamService sysTeamService;

    /**
     * 班组管理列表
     */
    @Operation(summary = "班组管理查询列表")
    @Parameters({
            @Parameter(name = "current", description = "当前页"),
            @Parameter(name = "size", description = "每页显示条数"),
            @Parameter(name = "teamName", description = "班组名称"),
            @Parameter(name = "factoryId", description = "所属工厂ID")
    })
    @GetMapping("/page")
    public Result<PageResult<SysTeam>> getPage(@Parameter(hidden = true) Page<SysTeam> page, @Parameter(hidden = true) SysTeam team) {
        return Result.ok(PageResult.<SysTeam>builder().build().pageResult(sysTeamService.getPage(page, team)));
    }

    /**
     * 通过工厂ID获取班组选择树形
     */
    @Operation(summary = " 通过工厂ID获取班组选择树形")
    @GetMapping("/tree")
    public Result<List<Tree<Long>>> tree(@Parameter(description = "所属工厂ID") @RequestParam("factoryIds") List<Long> factoryIds) {
        return Result.ok(sysTeamService.tree(factoryIds));
    }


    /**
     * 通过工厂ID获取班组选择列表
     */
    @Operation(summary = " 通过工厂ID获取班组选择列表")
    @GetMapping("/list")
    public Result<List<SysTeam>> list(@Parameter(description = "所属工厂ID") @RequestParam(value = "factoryId", required = false) Long factoryId) {
        return Result.ok(sysTeamService.list(Wrappers.<SysTeam>lambdaQuery().eq(factoryId != null, SysTeam::getFactoryId, factoryId).orderByAsc(SysTeam::getSortOrder)));
    }

    /**
     * 班组管理查询
     */
    @Operation(summary = "班组管理查询")
    @Parameter(name = "id", description = "班组管理ID", required = true)
    @GetMapping("/{id:\\d+}")
    public Result<SysTeam> findSysTeamById(@PathVariable Long id){
        return Result.ok(sysTeamService.getById(id));
    }

    /**
     * 班组管理新增
     */
    @Operation(summary = "班组管理新增")
    @PostMapping
    public Result<Boolean> save(@RequestBody @Valid SysTeam sysTeam){
        return Result.ok(sysTeamService.save(sysTeam));
    }

    /**
     * 班组管理修改
     */
    @Operation(summary = "班组管理修改")
    @PutMapping
    public Result<Boolean> update(@RequestBody @Valid SysTeam sysTeam){
        return Result.ok(sysTeamService.updateById(sysTeam));
    }

    /**
     * 班组管理删除
     */
    @Operation(summary = "班组管理删除")
    @Parameter(name = "id", description = "班组管理ID", required = true)
    @DeleteMapping("/{id:\\d+}")
    public Result<Boolean> delete(@PathVariable Long id){
        return Result.ok(sysTeamService.removeById(id));
        }
        }
