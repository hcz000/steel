package com.gv.steel.system.base.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gv.steel.common.core.exception.BaseException;
import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.system.base.dao.AnnealModelDao;
import com.gv.steel.system.base.entity.AnnealModel;
import com.gv.steel.system.base.service.AnnealModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 退火模型表 服务实现类
 * </p>
 *
 * @author administrator
 * @since 2025-05-12
 */
@Slf4j
@Service
@Transactional
public class AnnealModelServiceImpl extends BaseServiceImpl<AnnealModelDao, AnnealModel> implements AnnealModelService {

	@Override
	public boolean saveAnnealModel(AnnealModel annealModel) {
		AnnealModel one = getOne(Wrappers.<AnnealModel>lambdaQuery().eq(AnnealModel::getCode, annealModel.getCode()));
		if (ObjUtil.isNotNull(one)) {
			throw new BaseException("该退火模型已存在");
		}

		return save(annealModel);
	}

	@Override
	public boolean updateAnnealModelById(AnnealModel annealModel) {
		AnnealModel one = getOne(Wrappers.<AnnealModel>lambdaQuery().eq(AnnealModel::getCode, annealModel.getCode()));
		if (ObjUtil.isNotNull(one) && !annealModel.getId().equals(one.getId())) {
			throw new BaseException("该退火模型已存在");
		}
		return updateById(annealModel);
	}
}
