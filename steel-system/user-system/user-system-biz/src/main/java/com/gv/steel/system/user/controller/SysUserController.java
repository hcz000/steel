package com.gv.steel.system.user.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.core.context.UserInfoContextHolder;
import com.gv.steel.common.core.util.MsgUtils;
import com.gv.steel.common.core.vo.UserInfo;
import com.gv.steel.common.easyexcel.annotation.EasyExcelExport;
import com.gv.steel.common.easyexcel.annotation.EasyExcelImport;
import com.gv.steel.common.easyexcel.annotation.Sheet;
import com.gv.steel.common.log.annotation.SysLog;
import com.gv.steel.common.security.annotation.Inner;
import com.gv.steel.common.security.util.SecurityUtils;
import com.gv.steel.common.xss.core.XssCleanIgnore;
import com.gv.steel.system.user.constant.ErrorCodeConstants;
import com.gv.steel.system.user.dto.UserDTO;
import com.gv.steel.system.user.entity.SysUser;
import com.gv.steel.system.user.service.SysUserService;
import com.gv.steel.system.user.vo.UserDetailVO;
import com.gv.steel.system.user.vo.UserExcelVO;
import com.gv.steel.system.user.vo.UserInfoVO;
import com.gv.steel.system.user.vo.UserVO;
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
import java.util.Set;

/**
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "用户管理模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysUserController {

    private final SysUserService userService;

    /**
     * 获取当前用户全部信息
     *
     * @return 用户信息
     */
    @Operation(summary = "获取当前用户全部信息")
    @GetMapping(value = {"/info"})
    public Result<UserInfoVO> info() {
        String username = SecurityUtils.getUser().getUsername();
        SysUser user = userService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getUsername, username));
        if (user == null) {
            return Result.failed(MsgUtils.getMessage(ErrorCodeConstants.SYS_USER_QUERY_ERROR));
        }
        user.setPassword(null);
        UserInfo userInfo = userService.getUserInfo(user);
        UserInfoVO vo = new UserInfoVO();
        vo.setSysUser(user);
        vo.setRoles(userInfo.getRoles());
        vo.setPermissions(userInfo.getPermissions());
        vo.setFactoryList(userInfo.getFactoryList());
        return Result.ok(vo);
    }

    /**
     * 获取指定用户全部信息
     *
     * @return 用户信息
     */
    @Inner
    @Operation(summary = "获取指定用户全部信息")
    @Parameter(name = "username", description = "用户名")
    @GetMapping("/info/{username}")
    public Result<UserInfo> info(@PathVariable String username) {
        SysUser user = userService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getUsername, username));
        if (user == null) {
            return Result.failed(MsgUtils.getMessage(ErrorCodeConstants.SYS_USER_USERINFO_EMPTY, username));
        }
        return Result.ok(userService.getUserInfo(user));
    }

    /**
     * 根据部门id，查询对应的用户 id 集合
     *
     * @param deptIds 部门id 集合
     * @return 用户 id 集合
     */
    @Inner
    @Operation(summary = "根据部门id，查询对应的用户 id 集合")
    @Parameter(name = "deptIds", description = "部门ID集合")
    @GetMapping("/ids")
    public Result<List<Long>> listUserIdByDeptIds(@RequestParam("deptIds") Set<Long> deptIds) {
        return Result.ok(userService.listUserIdByDeptIds(deptIds));
    }

    /**
     * 通过ID查询用户信息
     *
     * @param id ID
     * @return 用户信息
     */
    @Operation(summary = "通过ID查询用户信息")
    @Parameter(name = "id", description = "用户ID")
    @GetMapping("/{id:\\d+}")
    public Result<UserDetailVO> user(@PathVariable Long id) {
        return Result.ok(userService.getUserVoById(id));
    }

    @Inner
    @Operation(summary = "通过ID查询用户基本信息", hidden = true)
    @Parameter(name = "id", description = "用户ID")
    @GetMapping("/base/{id:\\d+}")
    public Result<SysUser> baseInfo(@PathVariable Long id) {
        return Result.ok(
                userService.getOne(
                        Wrappers.<SysUser>lambdaQuery()
                                .eq(SysUser::getId, id)
                                .select(Lists.newArrayList(SysUser::getId, SysUser::getUsername, SysUser::getNickname, SysUser::getPhone, SysUser::getDeptId))
                )
        );
    }

    /**
     * 判断用户是否存在
     *
     * @param userDTO 查询条件
     * @return
     */
    @Inner(false)
    @Operation(summary = "判断用户是否存在")
    @Parameters({
            @Parameter(name = "username", description = "用户名"),
    })
    @GetMapping("/check/exist")
    public Result<Boolean> isExist(@Parameter(hidden = true) UserDTO userDTO) {
        List<SysUser> sysUserList = userService.list(new QueryWrapper<>(userDTO));
        if (CollUtil.isNotEmpty(sysUserList)) {
            return Result.ok(Boolean.TRUE, MsgUtils.getMessage(ErrorCodeConstants.SYS_USER_EXISTING));
        }
        return Result.ok(Boolean.FALSE);
    }

    /**
     * 删除用户信息
     *
     * @param id ID
     * @return Result
     */
    @SysLog("删除用户信息")
    @Operation(summary = "删除用户信息")
    @Parameter(name = "id", description = "用户ID")
    @DeleteMapping("/{id:\\d+}")
    @PreAuthorize("@pms.hasPermission('sys_user_del')")
    public Result<Boolean> userDel(@PathVariable Long id) {
        SysUser sysUser = userService.getById(id);
        return Result.ok(userService.removeUserById(sysUser));
    }

    /**
     * 添加用户
     *
     * @param userDto 用户信息
     * @return success/false
     */
    @SysLog("添加用户")
    @Operation(summary = "添加用户")
    @PostMapping
    @XssCleanIgnore({"password"})
    @PreAuthorize("@pms.hasPermission('sys_user_add')")
    public Result<Boolean> user(@RequestBody UserDTO userDto) {
        return Result.ok(userService.saveUser(userDto));
    }

    /**
     * 管理员更新用户信息
     *
     * @param userDto 用户信息
     * @return Result
     */
    @SysLog("更新用户信息")
    @Operation(summary = "更新用户信息")
    @PutMapping
    @XssCleanIgnore({"password"})
    @PreAuthorize("@pms.hasPermission('sys_user_edit')")
    public Result<Boolean> updateUser(@Valid @RequestBody UserDTO userDto) {
        return userService.updateUser(userDto);
    }

    /**
     * 分页查询用户
     *
     * @param page    参数集
     * @param userDTO 查询参数列表
     * @return 用户集合
     */
    @Operation(summary = "分页查询用户", description = "分页查询用户")
    @Parameters({
            @Parameter(name = "size", description = "每页显示条数"),
            @Parameter(name = "current", description = "当前页"),
            @Parameter(name = "nickname", description = "用户昵称"),
            @Parameter(name = "phone", description = "手机号"),
            @Parameter(name = "role", description = "角色ID集合"),
            @Parameter(name = "deptId", description = "部门ID"),
    })
    @GetMapping("/page")
    public Result<PageResult<UserVO>> getUserPage(@Parameter(hidden = true) Page<UserVO> page,
                                                  @Parameter(hidden = true) UserDTO userDTO) {
        return Result.ok(PageResult.<UserVO>builder().build().pageResult(userService.getUserWithRolePage(page, userDTO)));
    }

    @Parameters({
            @Parameter(name = "nickname", description = "用户昵称"),
            @Parameter(name = "phone", description = "手机号"),
            @Parameter(name = "role", description = "角色ID集合"),
            @Parameter(name = "deptId", description = "部门ID"),
            @Parameter(name = "factoryIds", description = "加工厂ID集合,format: xx,xx")
    })
    @Operation(summary = "查询全部用户下拉列表", description = "查询全部用户下拉列表")
    @GetMapping("/list")
    public Result<List<UserVO>> getSysUserList(@Parameter(hidden = true) UserDTO userDTO) {
        return Result.ok(userService.getUserList(userDTO));
    }

    /**
     * 个人修改个人信息
     *
     * @param userDto userDto
     * @return success/false
     */
    @SysLog("修改个人信息")
    @Operation(summary = "修改个人信息")
    @PutMapping("/edit")
    @XssCleanIgnore({"password", "newpassword1"})
    public Result<Boolean> updateUserInfo(@Valid @RequestBody UserDTO userDto) {
        userDto.setId(UserInfoContextHolder.currentUserId());
        userDto.setUsername(SecurityUtils.getUser().getUsername());
        return userService.updateUserInfo(userDto);
    }

    /**
     * @param username 用户名称
     * @return 上级部门用户列表
     */
    @Operation(summary = "查询上级部门的用户信息")
    @Parameter(name = "username", description = "用户名")
    @GetMapping("/ancestor/{username}")
    public Result<List<SysUser>> listAncestorUsers(@PathVariable String username) {
        return Result.ok(userService.listAncestorUsersByUsername(username));
    }

    /**
     * 导出excel 表格
     *
     * @param userDTO 查询条件
     * @return
     */
    @Operation(summary = "导出excel 表格")
    @Parameters({
            @Parameter(name = "role", description = "角色ID集合"),
            @Parameter(name = "deptId", description = "部门ID"),
    })
    @EasyExcelExport(name = "用户信息", sheets = {@Sheet(sheetName = "用户信息")})
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('sys_user_import_export')")
    public List<UserExcelVO> export(@Parameter(hidden = true) UserDTO userDTO) {
        return userService.listUser(userDTO);
    }

    /**
     * 导入用户
     *
     * @param excelVOList   用户列表
     * @param bindingResult 错误信息列表
     * @return Result
     */
    @PostMapping("/import")
    @PreAuthorize("@pms.hasPermission('sys_user_import_export')")
    public Result importUser(@EasyExcelImport List<UserExcelVO> excelVOList, BindingResult bindingResult) {
        return userService.importUser(excelVOList, bindingResult);
    }

}
