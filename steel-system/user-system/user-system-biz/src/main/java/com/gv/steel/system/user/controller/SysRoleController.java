package com.gv.steel.system.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.easyexcel.annotation.EasyExcelExport;
import com.gv.steel.common.easyexcel.annotation.EasyExcelImport;
import com.gv.steel.common.log.annotation.SysLog;
import com.gv.steel.common.mybatis.utils.QueryWrapperUtil;
import com.gv.steel.system.user.dto.SysRoleQueryDTO;
import com.gv.steel.system.user.entity.SysRole;
import com.gv.steel.system.user.service.SysRoleMenuService;
import com.gv.steel.system.user.service.SysRoleService;
import com.gv.steel.system.user.vo.RoleExcelVO;
import com.gv.steel.system.user.vo.RoleVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/role")
@Tag(name = "角色管理模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysRoleController {

    private final SysRoleService sysRoleService;

    private final SysRoleMenuService sysRoleMenuService;

    /**
     * 通过ID查询角色信息
     *
     * @param id ID
     * @return 角色信息
     */
    @Operation(summary = "通过ID查询角色信息", description = "通过ID查询角色信息")
    @Parameter(name = "id", description = "角色ID")
    @GetMapping("/{id:\\d+}")
    public Result<SysRole> getById(@PathVariable Long id) {
        return Result.ok(sysRoleService.getById(id));
    }

    /**
     * 添加角色
     *
     * @param sysRole 角色信息
     * @return success、false
     */
    @Operation(summary = "添加角色", description = "添加角色")
    @SysLog("添加角色")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sys_role_add')")
    public Result<Boolean> save(@Valid @RequestBody SysRole sysRole) {
        return Result.ok(sysRoleService.save(sysRole));
    }

    /**
     * 修改角色
     *
     * @param sysRole 角色信息
     * @return success/false
     */
    @Operation(summary = "修改角色", description = "修改角色")
    @SysLog("修改角色")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('sys_role_edit')")
    public Result<Boolean> update(@Valid @RequestBody SysRole sysRole) {
        return Result.ok(sysRoleService.updateById(sysRole));
    }

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    @Operation(summary = "删除角色", description = "删除角色")
    @Parameter(name = "id", description = "角色ID")
    @SysLog("删除角色")
    @DeleteMapping("/{id:\\d+}")
    @PreAuthorize("@pms.hasPermission('sys_role_del')")
    public Result<Boolean> removeById(@PathVariable Long id) {
        return Result.ok(sysRoleService.removeRoleById(id));
    }

    /**
     * 获取角色列表
     *
     * @return 角色列表
     */
    @Operation(summary = "获取角色列表", description = "获取角色列表")
    @GetMapping("/list")
    public Result<List<SysRole>> listRoles() {
        return Result.ok(sysRoleService.list(Wrappers.emptyWrapper()));
    }

    /**
     * 分页查询角色信息
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @Operation(summary = "分页查询角色信息", description = "分页查询角色信息")
    @Parameters({
            @Parameter(name = "current", description = "当前页"),
            @Parameter(name = "size", description = "每页显示条数"),
            @Parameter(name = "roleName", description = "角色名称"),
            @Parameter(name = "roleDesc", description = "角色描述"),
    })
    @GetMapping("/page")
    public Result<PageResult<SysRole>> getRolePage(@Parameter(hidden = true) Page<SysRole> page, @Parameter(hidden = true) SysRoleQueryDTO dto) {
        QueryWrapper<SysRole> wrapper = QueryWrapperUtil.queryWrapperHandler(dto);
        return Result.ok(PageResult.<SysRole>builder().build().pageResult(sysRoleService.page(page, wrapper)));
    }

    /**
     * 更新角色菜单
     *
     * @param roleVo 角色对象
     * @return success、false
     */
    @Operation(summary = "更新角色菜单", description = "更新角色菜单")
    @SysLog("更新角色菜单")
    @PutMapping("/menu")
    @PreAuthorize("@pms.hasPermission('sys_role_perm')")
    public Result<Boolean> saveRoleMenus(@RequestBody RoleVo roleVo) {
        return Result.ok(sysRoleMenuService.saveRoleMenus(roleVo.getRoleId(), roleVo.getMenuIds()));
    }

    /**
     * 导出excel 表格
     *
     * @return
     */
    @Operation(summary = "导出excel 表格", description = "导出excel 表格")
    @EasyExcelExport
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('sys_role_import_export')")
    public List<RoleExcelVO> export() {
        return sysRoleService.listRole();
    }

    /**
     * 导入角色
     *
     * @param excelVOList   角色列表
     * @param bindingResult 错误信息列表
     * @return ok fail
     */
    @Operation(summary = "导入角色", description = "导入角色")
    @PostMapping("/import")
    @PreAuthorize("@pms.hasPermission('sys_role_import_export')")
    public Result<?> importRole(@Parameter(hidden = true) @EasyExcelImport List<RoleExcelVO> excelVOList, @Parameter(hidden = true) BindingResult bindingResult) {
        return sysRoleService.importRole(excelVOList, bindingResult);
    }

}
