package com.gv.steel.system.user.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.log.annotation.SysLog;
import com.gv.steel.common.security.annotation.Inner;
import com.gv.steel.system.user.entity.SysOauthClientDetails;
import com.gv.steel.system.user.service.SysOauthClientDetailsService;
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
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/client")
@Tag(name = "客户端管理模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysOauthClientDetailsController {

    private final SysOauthClientDetailsService sysOauthClientDetailsService;

    /**
     * 通过ID查询
     *
     * @param clientId 客户端id
     * @return SysOauthClientDetails
     */
    @Operation(summary = "通过ID查询", description = "通过客户端ID查询")
    @Parameter(name = "clientId", description = "客户端id")
    @GetMapping("/{clientId}")
    public Result<List<SysOauthClientDetails>> getByClientId(@PathVariable String clientId) {
        return Result.ok(sysOauthClientDetailsService
                .list(Wrappers.<SysOauthClientDetails>lambdaQuery().eq(SysOauthClientDetails::getClientId, clientId)));
    }

    /**
     * 简单分页查询
     *
     * @param page                  分页对象
     * @param sysOauthClientDetails 系统终端
     * @return
     */
    @Operation(summary = "简单分页查询", description = "简单分页查询")
    @Parameters({
            @Parameter(name = "current", description = "当前页"),
            @Parameter(name = "size", description = "每页显示条数"),
            @Parameter(name = "clientId", description = "客户端ID"),
    })
    @GetMapping("/page")
    public Result<PageResult<SysOauthClientDetails>> getOauthClientDetailsPage(@Parameter(hidden = true) Page<SysOauthClientDetails> page,
                                                                               @Parameter(hidden = true) SysOauthClientDetails sysOauthClientDetails) {
        return Result.ok(PageResult.<SysOauthClientDetails>builder().build()
                .pageResult(sysOauthClientDetailsService.page(page, Wrappers.query(sysOauthClientDetails)))
        );
    }

    /**
     * 添加
     *
     * @param sysOauthClientDetails 实体
     * @return success/false
     */
    @Operation(summary = "添加终端", description = "添加终端")
    @SysLog("添加终端")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sys_client_add')")
    public Result<Boolean> add(@Valid @RequestBody SysOauthClientDetails sysOauthClientDetails) {
        return Result.ok(sysOauthClientDetailsService.save(sysOauthClientDetails));
    }

    /**
     * 删除
     *
     * @param id ID
     * @return success/false
     */
    @Operation(summary = "删除终端", description = "删除终端")
    @Parameter(name = "id", description = "客户端ID")
    @SysLog("删除终端")
    @DeleteMapping("/{id:\\d+}")
    @PreAuthorize("@pms.hasPermission('sys_client_del')")
    public Result<Boolean> removeById(@PathVariable Long id) {
        return Result.ok(sysOauthClientDetailsService.removeClientDetailsById(id));
    }

    /**
     * 编辑
     *
     * @param sysOauthClientDetails 实体
     * @return success/false
     */
    @Operation(summary = "编辑终端", description = "编辑终端")
    @SysLog("编辑终端")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('sys_client_edit')")
    public Result<Boolean> update(@Valid @RequestBody SysOauthClientDetails sysOauthClientDetails) {
        return Result.ok(sysOauthClientDetailsService.updateClientDetailsById(sysOauthClientDetails));
    }

    @Operation(summary = "清除终端缓存", description = "清除终端缓存")
    @SysLog("清除终端缓存")
    @DeleteMapping("/cache")
    @PreAuthorize("@pms.hasPermission('sys_client_del')")
    public Result<Boolean> clearClientCache() {
        sysOauthClientDetailsService.clearClientCache();
        return Result.ok();
    }

    @Operation(summary = "通过终端ID获取终端信息", description = "通过终端ID获取终端信息")
    @Parameter(name = "clientId", description = "客户端ID")
    @Inner
    @GetMapping("/getClientDetailsById/{clientId}")
    public Result<SysOauthClientDetails> getClientDetailsById(@PathVariable String clientId) {
        return Result.ok(sysOauthClientDetailsService.getOne(
                Wrappers.<SysOauthClientDetails>lambdaQuery().eq(SysOauthClientDetails::getClientId, clientId), false));
    }

}
