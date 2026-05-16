package com.gv.steel.system.base.service;

import com.gv.steel.common.mybatis.base.service.BaseService;
import com.gv.steel.system.base.entity.AnnealModel;

/**
 * <p>
 * 退火模型表 服务类
 * </p>
 *
 * @author administrator
 * @since 2025-05-12
 */
public interface AnnealModelService extends BaseService<AnnealModel> {

	boolean saveAnnealModel(AnnealModel annealModel);

	boolean updateAnnealModelById(AnnealModel annealModel);
}
