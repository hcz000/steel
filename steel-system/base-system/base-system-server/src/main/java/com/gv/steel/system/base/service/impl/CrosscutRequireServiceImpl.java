package com.gv.steel.system.base.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.system.base.dao.CrosscutRequireDao;
import com.gv.steel.system.base.entity.CrosscutRequire;
import com.gv.steel.system.base.service.CrosscutRequireService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 横切基本要求 服务实现类
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Slf4j
@Service
@Transactional
public class CrosscutRequireServiceImpl extends BaseServiceImpl<CrosscutRequireDao, CrosscutRequire> implements CrosscutRequireService {

    @Override
    public CrosscutRequire getByTableId(Long tableId, String tableName) {
        return this.getOne(Wrappers.<CrosscutRequire>lambdaQuery()
                .eq(CrosscutRequire::getTableId, tableId).eq(CrosscutRequire::getTableName, tableName));
    }
}
