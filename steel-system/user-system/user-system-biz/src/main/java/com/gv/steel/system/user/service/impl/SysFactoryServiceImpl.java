package com.gv.steel.system.user.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.system.user.dao.SysFactoryDao;
import com.gv.steel.system.user.entity.SysFactory;
import com.gv.steel.system.user.service.SysFactoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 工厂管理 服务实现类
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Slf4j
@Service
@Transactional
public class SysFactoryServiceImpl extends BaseServiceImpl<SysFactoryDao, SysFactory> implements SysFactoryService {

    @Override
    public Page<SysFactory> getPage(Page<SysFactory> page, SysFactory factory) {
        LambdaQueryWrapper<SysFactory> wrapper = getWrapper(factory);
        return this.page(page, wrapper);
    }

    private LambdaQueryWrapper<SysFactory> getWrapper(SysFactory factory) {
        LambdaQueryWrapper<SysFactory> wrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotEmpty(factory)) {
            if (StrUtil.isNotEmpty(factory.getFactoryName())) {
                wrapper.like(SysFactory::getFactoryName, factory.getFactoryName());
            }
            if (StrUtil.isNotEmpty(factory.getAddress())) {
                wrapper.like(SysFactory::getAddress, factory.getAddress());
            }
        }
        return wrapper;
    }
}
