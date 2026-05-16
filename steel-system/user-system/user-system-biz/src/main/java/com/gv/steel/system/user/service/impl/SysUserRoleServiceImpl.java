package com.gv.steel.system.user.service.impl;

import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.system.user.dao.SysUserRoleDao;
import com.gv.steel.system.user.entity.SysUserRole;
import com.gv.steel.system.user.service.SysUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 */
@Service
@Transactional
public class SysUserRoleServiceImpl extends BaseServiceImpl<SysUserRoleDao, SysUserRole> implements SysUserRoleService {

}
