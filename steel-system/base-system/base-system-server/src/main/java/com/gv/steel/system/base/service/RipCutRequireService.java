package com.gv.steel.system.base.service;

import com.gv.steel.common.mybatis.base.service.BaseService;
import com.gv.steel.system.base.entity.RipCutRequire;

/**
 * <p>
 * 纵切基本要求 服务类
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
public interface RipCutRequireService extends BaseService<RipCutRequire> {

    /**
     * 通过业务ID查询纵切基本要求
     *
     * @param tableId 业务ID
     * @return RipCutRequire
     */
    RipCutRequire getByTableId(Long tableId, String tableName);
}
