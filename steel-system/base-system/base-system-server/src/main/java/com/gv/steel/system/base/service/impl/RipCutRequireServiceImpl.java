package com.gv.steel.system.base.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.system.base.dao.RipCutRequireDao;
import com.gv.steel.system.base.entity.RipCutRequire;
import com.gv.steel.system.base.service.RipCutRequireService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>
 * 纵切基本要求 服务实现类
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Slf4j
@Service
@Transactional
public class RipCutRequireServiceImpl extends BaseServiceImpl<RipCutRequireDao, RipCutRequire> implements RipCutRequireService {

    @Override
    public RipCutRequire getByTableId(Long tableId, String tableName) {
        return this.getOne(Wrappers.<RipCutRequire>lambdaQuery()
                .eq(RipCutRequire::getTableId, tableId).eq(RipCutRequire::getTableName, tableName));
    }
}
