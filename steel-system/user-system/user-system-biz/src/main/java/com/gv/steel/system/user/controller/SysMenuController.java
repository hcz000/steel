package com.gv.steel.system.user.controller;

import cn.hutool.core.lang.tree.Tree;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.log.annotation.SysLog;
import com.gv.steel.common.security.util.SecurityUtils;
import com.gv.steel.system.user.entity.SysMenu;
import com.gv.steel.system.user.service.SysMenuService;
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
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menu")
@Tag(name = "菜单管理模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysMenuController {

    private final SysMenuService sysMenuService;

    /**
     * 返回当前用户的树形菜单集合
     *
     * @param parentId 父节点ID
     * @return 当前用户的树形菜单
     */
    @Operation(summary = "返回当前用户的树形菜单集合", description = "返回当前用户的树形菜单集合")
    @Parameter(name = "parentId", description = "父级ID，为空则获取当前用户全部菜单树")
    @GetMapping
    public Result<List<Tree<Long>>> getUserMenu(@RequestParam(required = false) Long parentId) {
        // 获取符合条件的菜单
        Set<SysMenu> menuSet = SecurityUtils.getRoles()
                .stream()
                .map(sysMenuService::findMenuByRoleId)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        return Result.ok(sysMenuService.filterMenu(menuSet, parentId));
    }

    /**
     * 返回树形菜单集合
     *
     * @param lazy     是否是懒加载
     * @param parentId 父节点ID
     * @return 树形菜单
     */
    @Operation(summary = "返回树形菜单集合", description = "返回树形菜单集合")
    @Parameters({
            @Parameter(name = "lazy", description = "是否懒加载"),
            @Parameter(name = "parentId", description = "父节点ID，为空则获取parentId为0的子菜单")
    })
    @GetMapping(value = "/tree")
    public Result<List<Tree<Long>>> getTree(@RequestParam(required = false) boolean lazy, @RequestParam(required = false) Long parentId) {
        return Result.ok(sysMenuService.treeMenu(lazy, parentId));
    }

    /**
     * 返回角色的菜单集合
     *
     * @param roleId 角色ID
     * @return 属性集合
     */
    @Operation(summary = "返回角色的菜单集合", description = "返回角色的菜单集合")
    @Parameter(name = "roleId", description = "角色ID")
    @GetMapping("/tree/{roleId}")
    public Result<List<Long>> getRoleTree(@PathVariable Long roleId) {
        return Result
                .ok(sysMenuService.findMenuByRoleId(roleId).stream().map(SysMenu::getId).collect(Collectors.toList()));
    }

    /**
     * 通过ID查询菜单的详细信息
     *
     * @param id 菜单ID
     * @return 菜单详细信息
     */
    @Operation(summary = "通过ID查询菜单的详细信息", description = "通过ID查询菜单的详细信息")
    @Parameter(name = "id", description = "菜单ID")
    @GetMapping("/{id:\\d+}")
    public Result<SysMenu> getById(@PathVariable Long id) {
        return Result.ok(sysMenuService.getById(id));
    }

    /**
     * 新增菜单
     *
     * @param sysMenu 菜单信息
     * @return 含ID 菜单信息
     */
    @Operation(summary = "新增菜单", description = "新增菜单")
    @SysLog("新增菜单")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sys_menu_add')")
    public Result<Boolean> save(@Valid @RequestBody SysMenu sysMenu) {
        return Result.ok(sysMenuService.saveMenu(sysMenu));
    }

    /**
     * 删除菜单
     *
     * @param id 菜单ID
     * @return success/false
     */
    @Operation(summary = "删除菜单", description = "删除菜单")
    @Parameter(name = "id", description = "菜单ID")
    @SysLog("删除菜单")
    @DeleteMapping("/{id:\\d+}")
    @PreAuthorize("@pms.hasPermission('sys_menu_del')")
    public Result<Boolean> removeById(@PathVariable Long id) {
        return Result.ok(sysMenuService.removeMenuById(id));
    }

    /**
     * 更新菜单
     *
     * @param sysMenu
     * @return
     */
    @Operation(summary = "更新菜单", description = "更新菜单")
    @SysLog("更新菜单")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('sys_menu_edit')")
    public Result<Boolean> update(@Valid @RequestBody SysMenu sysMenu) {
        return Result.ok(sysMenuService.updateMenuById(sysMenu));
    }

    /**
     * 清除菜单缓存
     */
    @Operation(summary = "清除菜单缓存", description = "清除菜单缓存")
    @SysLog("清除菜单缓存")
    @DeleteMapping("/cache")
    @PreAuthorize("@pms.hasPermission('sys_menu_del')")
    public Result<Boolean> clearMenuCache() {
        sysMenuService.clearMenuCache();
        return Result.ok();
    }

}
