package com.gv.steel.system.base.service;

import com.gv.steel.common.mybatis.base.service.BaseService;
import com.gv.steel.system.base.entity.RollingRequire;

/**
 * <p>
 * 压延基本要求 服务类
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
public interface RollingRequireService extends BaseService<RollingRequire> {

    /**
     * 通过业务ID查询压延基本要求
     *
     * @param tableId 业务ID
     * @return RollingRequire
     */
    RollingRequire getByTableId(Long tableId, String tableName);
}
