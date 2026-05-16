package com.gv.steel.system.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.core.constant.CacheConstants;
import com.gv.steel.common.core.entity.SysTeamInfo;
import com.gv.steel.common.core.enums.MenuTypeEnum;
import com.gv.steel.common.core.exception.BaseException;
import com.gv.steel.common.core.util.MsgUtils;
import com.gv.steel.common.core.vo.UserInfo;
import com.gv.steel.common.easyexcel.model.ErrorMessage;
import com.gv.steel.common.mybatis.base.entity.CommonModel;
import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.system.user.constant.ErrorCodeConstants;
import com.gv.steel.system.user.convert.SysFactoryMapper;
import com.gv.steel.system.user.convert.SysRoleMapper;
import com.gv.steel.system.user.convert.SysTeamMapper;
import com.gv.steel.system.user.convert.SysUserMapper;
import com.gv.steel.system.user.dao.*;
import com.gv.steel.system.user.dto.UserDTO;
import com.gv.steel.system.user.entity.*;
import com.gv.steel.system.user.service.AppService;
import com.gv.steel.system.user.service.SysMenuService;
import com.gv.steel.system.user.service.SysTeamService;
import com.gv.steel.system.user.service.SysUserService;
import com.gv.steel.system.user.vo.UserDetailVO;
import com.gv.steel.system.user.vo.UserExcelVO;
import com.gv.steel.system.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SysUserServiceImpl extends BaseServiceImpl<SysUserDao, SysUser> implements SysUserService {

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    private final AppService appService;

    private final SysRoleDao sysRoleDao;

    private final SysDeptDao sysDeptDao;

    private final SysUserFactoryDao sysUserFactoryDao;

    private final SysUserTeamDao sysUserTeamDao;

    private final SysMenuService sysMenuService;

    private final SysUserRoleDao sysUserRoleDao;

    private final SysTeamService sysTeamService;

    private final SysUserMapper sysUserMapper;

    private final SysRoleMapper sysRoleMapper;

    private final SysFactoryMapper sysFactoryMapper;

    private final SysFactoryDao sysFactoryDao;

    private final SysTeamMapper sysTeamMapper;

    /**
     * 保存用户信息
     *
     * @param userDto DTO 对象
     * @return success/fail
     */
    @Override
    public Boolean saveUser(UserDTO userDto) {
		SysUser dbUser = baseDao.selectOne(
				Wrappers.<SysUser>lambdaQuery()
						.eq(SysUser::getUsername, userDto.getUsername())
		);
		if (ObjUtil.isNotNull(dbUser)) {
			throw new BaseException(MsgUtils.getMessage(ErrorCodeConstants.SYS_USER_EXISTING));
		}
		SysUser sysUser = sysUserMapper.convert(userDto);
        sysUser.setPassword(ENCODER.encode(userDto.getPassword()));
        baseMapper.insert(sysUser);
        userDto.getRole().stream().map(roleId -> {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(sysUser.getId());
            userRole.setRoleId(roleId);
            return userRole;
        }).forEach(sysUserRoleDao::insert);
        userDto.getFactoryIds().stream().map(factoryId -> {
            SysUserFactory userFactory = new SysUserFactory();
            userFactory.setFactoryId(factoryId);
            userFactory.setUserId(sysUser.getId());
            return userFactory;
        }).forEach(sysUserFactoryDao::insert);
        if (ObjUtil.isNotEmpty(userDto.getTeamIds())) {
            List<SysTeam> teamList = sysTeamService.list(Wrappers.<SysTeam>lambdaQuery().in(CommonModel::getId, userDto.getTeamIds()));
            Map<Long, Long> teamMap = teamList.stream().collect(Collectors.toMap(CommonModel::getId, SysTeam::getFactoryId));

            userDto.getTeamIds().stream().map(teamId -> {
                SysUserTeam userTeam = new SysUserTeam();
                userTeam.setFactoryId(teamMap.get(teamId));
                userTeam.setUserId(sysUser.getId());
                userTeam.setTeamId(teamId);
                return userTeam;
            }).forEach(sysUserTeamDao::insert);
        }
        return Boolean.TRUE;
    }

    /**
     * 通过查用户的全部信息
     *
     * @param sysUser 用户
     * @return
     */
    @Override
    public UserInfo getUserInfo(SysUser sysUser) {
        UserInfo userInfo = sysUserMapper.convertUserInfo(sysUser);
        // 设置角色列表
        List<SysRole> roleList = sysRoleDao.listRolesByUserId(sysUser.getId());
        userInfo.setRoleList(sysRoleMapper.cnvertList(roleList));
        // 设置角色列表 （ID）
        List<Long> roleIds = roleList.stream().map(SysRole::getId).collect(Collectors.toList());
        userInfo.setRoles(ArrayUtil.toArray(roleIds, Long.class));
        // 设置用户工厂列表
        List<SysFactory> factoryList = sysFactoryDao.listFactoriesByUserId(sysUser.getId());
        userInfo.setFactoryList(sysFactoryMapper.cnvertInfoList(factoryList));
        // 设置用户班组列表
        List<SysTeam> teamList = sysTeamService.listTeamByUserId(sysUser.getId());
        userInfo.getFactoryList().forEach(f -> {
            List<SysTeamInfo> teamInfoList = sysTeamMapper.convertTempInfoList(
                    teamList.stream()
                            .filter(t -> t.getFactoryId().equals(f.getId()))
                            .collect(Collectors.toList())
            );
            f.setTeamInfoList(teamInfoList);
        });
        // 设置权限列表（menu.permission）
        Set<String> permissions = roleIds.stream()
                .map(sysMenuService::findMenuByRoleId)
                .flatMap(Collection::stream)
                .filter(m -> MenuTypeEnum.BUTTON.getType().equals(m.getType()))
                .map(SysMenu::getPermission)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toSet());
        userInfo.setPermissions(ArrayUtil.toArray(permissions, String.class));

        return userInfo;
    }

    /**
     * 分页查询用户信息（含有角色信息）
     *
     * @param page    分页对象
     * @param userDTO 参数列表
     * @return
     */
    @Override
    public Page<UserVO> getUserWithRolePage(Page page, UserDTO userDTO) {
        return baseMapper.getUserVosPage(page, userDTO);
    }

    /**
     * 通过ID查询用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @Override
    public UserDetailVO getUserVoById(Long id) {
        return baseMapper.getUserVoById(id);
    }

    /**
     * 删除用户
     *
     * @param sysUser 用户
     * @return Boolean
     */
    @Override
    public Boolean removeUserById(SysUser sysUser) {
        sysUserRoleDao.deleteByUserId(sysUser.getId());
        this.removeById(sysUser.getId());
        return Boolean.TRUE;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheConstants.USER_DETAILS, key = "#userDto.username"),
            @CacheEvict(value = CacheConstants.USER_INFO_DETAILS, key = "#userDto.id")
    })
    public Result<Boolean> updateUserInfo(UserDTO userDto) {
        UserVO userVO = baseMapper.getUserVoByUsername(userDto.getUsername());

//        // 判断手机号是否修改,更新手机号校验验证码
//        if (!StrUtil.equals(userVO.getPhone(), userDto.getPhone())) {
//            if (!appService.check(userDto.getPhone(), userDto.getCode())) {
//                return Result.failed(MsgUtils.getMessage(ErrorCodeConstants.SYS_APP_SMS_ERROR));
//            }
//        }

        // 修改密码逻辑
        SysUser sysUser = new SysUser();
        if (StrUtil.isNotBlank(userDto.getNewPassword())) {
            Assert.isTrue(ENCODER.matches(userDto.getPassword(), userVO.getPassword()),
                    MsgUtils.getMessage(ErrorCodeConstants.SYS_USER_UPDATE_PASSWORDERROR));
            sysUser.setPassword(ENCODER.encode(userDto.getNewPassword()));
        }
        sysUser.setPhone(userDto.getPhone());
        sysUser.setId(userVO.getId());
        sysUser.setAvatar(userDto.getAvatar());
        return Result.ok(this.updateById(sysUser));
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheConstants.USER_DETAILS, key = "#userDto.username"),
            @CacheEvict(value = CacheConstants.USER_INFO_DETAILS, key = "#userDto.id")
    })
    public Result<Boolean> updateUser(UserDTO userDto) {
        SysUser sysUser = sysUserMapper.convert(userDto);

        if (StrUtil.isNotBlank(userDto.getPassword())) {
            sysUser.setPassword(ENCODER.encode(userDto.getPassword()));
        }
        this.updateById(sysUser);

        //删除中间表
        sysUserRoleDao
                .delete(Wrappers.<SysUserRole>update().lambda().eq(SysUserRole::getUserId, userDto.getId()));
        sysUserFactoryDao
                .delete(Wrappers.<SysUserFactory>update().lambda().eq(SysUserFactory::getUserId, userDto.getId()));
        sysUserTeamDao
                .delete(Wrappers.<SysUserTeam>update().lambda().eq(SysUserTeam::getUserId, userDto.getId()));

        userDto.getRole().forEach(roleId -> {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(sysUser.getId());
            userRole.setRoleId(roleId);
            userRole.insert();
        });
        userDto.getFactoryIds().stream().map(factoryId -> {
            SysUserFactory userFactory = new SysUserFactory();
            userFactory.setFactoryId(factoryId);
            userFactory.setUserId(sysUser.getId());
            return userFactory;
        }).forEach(sysUserFactoryDao::insert);
        if (ObjUtil.isNotEmpty(userDto.getTeamIds())) {
            List<SysTeam> teamList = sysTeamService.list(Wrappers.<SysTeam>lambdaQuery().in(CommonModel::getId, userDto.getTeamIds()));
            Map<Long, Long> teamMap = teamList.stream().collect(Collectors.toMap(CommonModel::getId, SysTeam::getFactoryId));

            userDto.getTeamIds().stream().map(teamId -> {
                SysUserTeam userTeam = new SysUserTeam();
                userTeam.setFactoryId(teamMap.get(teamId));
                userTeam.setUserId(sysUser.getId());
                userTeam.setTeamId(teamId);
                return userTeam;
            }).forEach(sysUserTeamDao::insert);
        }
        return Result.ok();
    }

    /**
     * 查询上级部门的用户信息
     *
     * @param username 用户名
     * @return R
     */
    @Override
    public List<SysUser> listAncestorUsersByUsername(String username) {
        SysUser sysUser = this.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getUsername, username));

        SysDept sysDept = sysDeptDao.selectById(sysUser.getDeptId());
        if (sysDept == null) {
            return null;
        }

        Long parentId = sysDept.getParentId();
        return this.list(Wrappers.<SysUser>query().lambda().eq(SysUser::getDeptId, parentId));
    }

    /**
     * 查询全部的用户
     *
     * @param userDTO 查询条件
     * @return list
     */
    @Override
    public List<UserExcelVO> listUser(UserDTO userDTO) {
        List<UserVO> voList = baseMapper.selectVoList(userDTO);
        // 转换成execl 对象输出
        return voList.stream().map(userVO -> {
            UserExcelVO excelVO = sysUserMapper.convertVO2Excel(userVO);
            String roleNameList = userVO.getRoleList()
                    .stream()
                    .map(SysRole::getRoleName)
                    .collect(Collectors.joining(StrUtil.COMMA));
            excelVO.setRoleNameList(roleNameList);
            return excelVO;
        }).collect(Collectors.toList());
    }

    /**
     * excel 导入用户, 插入正确的 错误的提示行号
     *
     * @param excelVOList   excel 列表数据
     * @param bindingResult 错误数据
     * @return ok fail
     */
    @Override
    public Result importUser(List<UserExcelVO> excelVOList, BindingResult bindingResult) {
        // TODO 导入用户信息
        // 通用校验获取失败的数据
        List<ErrorMessage> errorMessageList = (List<ErrorMessage>) bindingResult.getTarget();

        // 个性化校验逻辑
        List<SysUser> userList = this.list();
        List<SysDept> deptList = sysDeptDao.selectList(Wrappers.emptyWrapper());
        List<SysRole> roleList = sysRoleDao.selectList(Wrappers.emptyWrapper());
        List<SysFactory> factoryList = sysFactoryDao.selectList(Wrappers.emptyWrapper());

        // 执行数据插入操作 组装 UserDto
        for (UserExcelVO excel : excelVOList) {
            Set<String> errorMsg = new HashSet<>();
            // 校验用户名是否存在
            boolean existUserName = userList.stream()
                    .anyMatch(sysUser -> excel.getUsername().equals(sysUser.getUsername()));

            if (existUserName) {
                errorMsg.add(MsgUtils.getMessage(ErrorCodeConstants.SYS_USER_USERNAME_EXISTING, excel.getUsername()));
            }

            // 判断输入的部门名称列表是否合法
            Optional<SysDept> deptOptional = deptList.stream()
                    .filter(dept -> excel.getDeptName().equals(dept.getName()))
                    .findFirst();
            if (!deptOptional.isPresent()) {
                errorMsg.add(MsgUtils.getMessage(ErrorCodeConstants.SYS_DEPT_DEPTNAME_INEXISTENCE, excel.getDeptName()));
            }

            // 判断输入的角色名称列表是否合法
            List<String> roleNameList = StrUtil.split(excel.getRoleNameList(), StrUtil.COMMA);
            List<SysRole> roleCollList = roleList.stream()
                    .filter(role -> roleNameList.stream().anyMatch(name -> role.getRoleName().equals(name)))
                    .collect(Collectors.toList());

            if (roleCollList.size() != roleNameList.size()) {
                errorMsg.add(MsgUtils.getMessage(ErrorCodeConstants.SYS_ROLE_ROLENAME_INEXISTENCE, excel.getRoleNameList()));
            }


            // 判断输入的工厂名称列表是否合法
            List<String> factoryNameList = StrUtil.split(excel.getFactoryNameList(), StrUtil.COMMA);
            List<SysFactory> factoryCollList = factoryList.stream()
                    .filter(factory -> factoryNameList.stream().anyMatch(name -> name.contains("总部") || factory.getFactoryNickname().equals(name)))
                    .collect(Collectors.toList());

            // 数据合法情况
            if (CollUtil.isEmpty(errorMsg)) {
                insertExcelUser(excel, deptOptional, roleCollList, factoryCollList);
            } else {
                // 数据不合法情况
                errorMessageList.add(new ErrorMessage(excel.getLineNum(), errorMsg));
            }

        }

        if (CollUtil.isNotEmpty(errorMessageList)) {
            return Result.failed(errorMessageList);
        }
        return Result.ok();
    }

    @Override
    public List<Long> listUserIdByDeptIds(Set<Long> deptIds) {
        return this.listObjs(
                Wrappers.lambdaQuery(SysUser.class).select(SysUser::getId).in(SysUser::getDeptId, deptIds),
                Long.class::cast);
    }

    /**
     * 插入excel User
     */
    private void insertExcelUser(UserExcelVO excel, Optional<SysDept> deptOptional, List<SysRole> roleCollList, List<SysFactory> factoryCollList) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(excel.getUsername());
        userDTO.setNickname(excel.getUsername());
        userDTO.setPhone(excel.getPhone());
        // 批量导入初始密码为手机号
        userDTO.setPassword("123456");
        // 根据部门名称查询部门ID
        userDTO.setDeptId(deptOptional.get().getId());
        // 根据角色名称查询角色ID
        List<Long> roleIdList = roleCollList.stream().map(SysRole::getId).collect(Collectors.toList());
        userDTO.setRole(roleIdList);
        // 根据工厂名称查询工厂ID
        List<Long> factoryIdList = factoryCollList.stream().map(SysFactory::getId).collect(Collectors.toList());
        userDTO.setFactoryIds(factoryIdList);
        // 插入用户
        this.saveUser(userDTO);
    }

    @Override
    public List<UserVO> getUserList(UserDTO userDTO) {
        return baseDao.selectVoList(userDTO);
    }

    /**
     * 注册用户 赋予用户默认角色
     *
     * @param userDto 用户信息
     * @return success/false
     */
    @Override
    public Result<Boolean> registerUser(UserDTO userDto) {
        // TODO 注册
//        // 校验验证码
//        if (!appService.check(userDto.getPhone(), userDto.getCode())) {
//            return Result.failed(MsgUtils.getMessage(ErrorCodeConstants.SYS_APP_SMS_ERROR));
//        }
//
//        // 判断用户名是否存在
//        SysUser sysUser = this.getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, userDto.getUsername()));
//        if (sysUser != null) {
//            return Result.failed(MsgUtils.getMessage(ErrorCodeConstants.SYS_USER_USERNAME_EXISTING, userDto.getUsername()));
//        }
//
//        // 获取默认角色编码
//        String defaultRole = ParamResolver.getStr("USER_DEFAULT_ROLE");
//        // 默认角色
//        SysRole sysRole = sysRoleDao
//                .selectOne(Wrappers.<SysRole>lambdaQuery().eq(SysRole::getRoleCode, defaultRole));
//
//        if (sysRole == null) {
//            return Result.failed(MsgUtils.getMessage(ErrorCodeConstants.SYS_PARAM_CONFIG_ERROR, "USER_DEFAULT_ROLE"));
//        }
//
//        userDto.setRole(Collections.singletonList(sysRole.getId()));
//        return Result.ok(saveUser(userDto));
        return Result.failed();
    }

}
