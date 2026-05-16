package com.gv.steel.system.user.service.impl;

import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.system.user.dao.SysUserTeamDao;
import com.gv.steel.system.user.entity.SysUserTeam;
import com.gv.steel.system.user.service.SysUserTeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户班组关联关系表 服务实现类
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Slf4j
@Service
@Transactional
public class SysUserTeamServiceImpl extends BaseServiceImpl<SysUserTeamDao, SysUserTeam> implements SysUserTeamService {

}
