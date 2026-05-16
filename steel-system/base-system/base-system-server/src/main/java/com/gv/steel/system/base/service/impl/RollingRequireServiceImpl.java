package com.gv.steel.system.base.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.system.base.dao.RollingRequireDao;
import com.gv.steel.system.base.entity.RollingRequire;
import com.gv.steel.system.base.service.RollingRequireService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 压延基本要求 服务实现类
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Slf4j
@Service
@Transactional
public class RollingRequireServiceImpl extends BaseServiceImpl<RollingRequireDao, RollingRequire> implements RollingRequireService {

    @Override
    public RollingRequire getByTableId(Long tableId, String tableName) {
        return this.getOne(Wrappers.<RollingRequire>lambdaQuery()
                .eq(RollingRequire::getTableId, tableId).eq(RollingRequire::getTableName, tableName));
    }
}
