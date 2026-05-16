package com.gv.steel.system.user.controller;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.log.annotation.SysLog;
import com.gv.steel.common.security.annotation.Inner;
import com.gv.steel.system.user.entity.SysDept;
import com.gv.steel.system.user.service.SysDeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
 * 部门管理 前端控制器
 * </p>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/dept")
@Tag(name = "部门管理模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysDeptController {

    private final SysDeptService sysDeptService;

    /**
     * 通过ID查询
     *
     * @param id ID
     * @return SysDept
     */
    @Operation(summary = "通过ID查询")
    @Parameter(name = "id", description = "部门ID")
    @GetMapping("/{id:\\d+}")
    public Result<SysDept> getById(@PathVariable Long id) {
        return Result.ok(sysDeptService.getById(id));
    }

    /**
     * 返回树形菜单集合
     *
     * @return 树形菜单
     */
    @Operation(summary = "返回树形菜单集合")
    @GetMapping(value = "/tree")
    public Result<List<Tree<Long>>> listDeptTrees() {
        return Result.ok(sysDeptService.listDeptTrees());
    }

    /**
     * 返回当前用户树形菜单集合
     *
     * @return 树形菜单
     */
    @Operation(summary = "返回当前用户树形菜单集合")
    @GetMapping(value = "/user-tree")
    public Result<List<Tree<Long>>> listCurrentUserDeptTrees() {
        return Result.ok(sysDeptService.listCurrentUserDeptTrees());
    }

    /**
     * 添加
     *
     * @param sysDept 实体
     * @return success/false
     */
    @Operation(summary = "添加部门")
    @SysLog("添加部门")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sys_dept_add')")
    public Result<Boolean> save(@Valid @RequestBody SysDept sysDept) {
        return Result.ok(sysDeptService.saveDept(sysDept));
    }

    /**
     * 删除
     *
     * @param id ID
     * @return success/false
     */
    @Operation(summary = "删除部门")
    @Parameter(name = "id", description = "部门ID")
    @SysLog("删除部门")
    @DeleteMapping("/{id:\\d+}")
    @PreAuthorize("@pms.hasPermission('sys_dept_del')")
    public Result<Boolean> removeById(@PathVariable Long id) {
        return Result.ok(sysDeptService.removeDeptById(id));
    }

    /**
     * 编辑
     *
     * @param sysDept 实体
     * @return success/false
     */
    @Operation(summary = "编辑部门")
    @SysLog("编辑部门")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('sys_dept_edit')")
    public Result<Boolean> update(@Valid @RequestBody SysDept sysDept) {
        return Result.ok(sysDeptService.updateDeptById(sysDept));
    }

    /**
     * 根据部门名查询部门信息
     *
     * @param deptName 部门名
     * @return SysDept
     */
    @Operation(summary = "根据部门名查询部门信息")
    @Parameter(name = "deptName", description = "部门名称")
    @GetMapping("/details/{deptName}")
    public Result<SysDept> user(@PathVariable String deptName) {
        SysDept condition = new SysDept();
        condition.setName(deptName);
        return Result.ok(sysDeptService.getOne(new QueryWrapper<>(condition)));
    }

    /**
     * 查收子级id列表
     *
     * @return 返回子级id列表
     */
    @Operation(summary = "查收子级id列表")
    @Parameter(name = "deptId", description = "部门ID")
    @Inner
    @GetMapping(value = "/child-id/{deptId:\\d+}")
    public Result<List<Long>> listChildDeptId(@PathVariable Long deptId) {
        return Result.ok(sysDeptService.listChildDeptId(deptId));
    }

}
