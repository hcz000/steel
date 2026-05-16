package com.gv.steel.system.base.service;

import com.gv.steel.common.mybatis.base.service.BaseService;
import com.gv.steel.system.base.entity.CrosscutRequire;

/**
 * <p>
 * 横切基本要求 服务类
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
public interface CrosscutRequireService extends BaseService<CrosscutRequire> {

    /**
     * 通过业务ID查询横切基本要求
     *
     * @param tableId 业务ID
     * @return CrosscutRequire
     */
    CrosscutRequire getByTableId(Long tableId, String tableName);
}
