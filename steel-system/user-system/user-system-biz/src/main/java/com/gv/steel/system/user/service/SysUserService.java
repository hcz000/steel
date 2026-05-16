package com.gv.steel.system.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.core.vo.UserInfo;
import com.gv.steel.common.mybatis.base.service.BaseService;
import com.gv.steel.system.user.dto.UserDTO;
import com.gv.steel.system.user.entity.SysUser;
import com.gv.steel.system.user.vo.UserDetailVO;
import com.gv.steel.system.user.vo.UserExcelVO;
import com.gv.steel.system.user.vo.UserVO;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Set;

public interface SysUserService extends BaseService<SysUser> {

    /**
     * 查询用户信息
     *
     * @param sysUser 用户
     * @return userInfo
     */
    UserInfo getUserInfo(SysUser sysUser);

    /**
     * 分页查询用户信息（含有角色信息）
     *
     * @param page    分页对象
     * @param userDTO 参数列表
     * @return
     */
    Page<UserVO> getUserWithRolePage(Page page, UserDTO userDTO);

    /**
     * 删除用户
     *
     * @param sysUser 用户
     * @return boolean
     */
    Boolean removeUserById(SysUser sysUser);

    /**
     * 更新当前用户基本信息
     *
     * @param userDto 用户信息
     * @return Boolean 操作成功返回true,操作失败返回false
     */
    Result<Boolean> updateUserInfo(UserDTO userDto);

    /**
     * 更新指定用户信息
     *
     * @param userDto 用户信息
     * @return
     */
    Result<Boolean> updateUser(UserDTO userDto);

    /**
     * 通过ID查询用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    UserDetailVO getUserVoById(Long id);

    /**
     * 查询上级部门的用户信息
     *
     * @param username 用户名
     * @return R
     */
    List<SysUser> listAncestorUsersByUsername(String username);

    /**
     * 保存用户信息
     *
     * @param userDto DTO 对象
     * @return success/fail
     */
    Boolean saveUser(UserDTO userDto);

    /**
     * 查询全部的用户
     *
     * @param userDTO 查询条件
     * @return list
     */
    List<UserExcelVO> listUser(UserDTO userDTO);

    /**
     * excel 导入用户
     *
     * @param excelVOList   excel 列表数据
     * @param bindingResult 错误数据
     * @return ok fail
     */
    Result importUser(List<UserExcelVO> excelVOList, BindingResult bindingResult);

    /**
     * 根据部门 id 列表查询对应的用户 id 集合
     *
     * @param deptIds 部门 id 列表
     * @return userIdList
     */
    List<Long> listUserIdByDeptIds(Set<Long> deptIds);

    /**
     * 注册用户
     *
     * @param userDto 用户信息
     * @return success/false
     */
    Result<Boolean> registerUser(UserDTO userDto);

    /**
     * 获取用户下拉列表
     *
     * @param userDTO 查询条件
     * @return list
     */
    List<UserVO> getUserList(UserDTO userDTO);
}
