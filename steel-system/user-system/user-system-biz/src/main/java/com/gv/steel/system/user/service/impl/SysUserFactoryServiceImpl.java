package com.gv.steel.system.user.service.impl;

import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.system.user.dao.SysUserFactoryDao;
import com.gv.steel.system.user.entity.SysUserFactory;
import com.gv.steel.system.user.service.SysUserFactoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户工厂关联关系表 服务实现类
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Slf4j
@Service
@Transactional
public class SysUserFactoryServiceImpl extends BaseServiceImpl<SysUserFactoryDao, SysUserFactory> implements SysUserFactoryService {

}
