package com.gv.steel.system.base.service.impl;

import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.system.base.dao.MaterialQualityLangDao;
import com.gv.steel.system.base.entity.MaterialQualityLang;
import com.gv.steel.system.base.service.MaterialQualityLangService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 材质语言表 服务实现类
 * </p>
 *
 * @author administrator
 * @since 2023-11-27
 */
@Slf4j
@Service
@Transactional
public class MaterialQualityLangServiceImpl extends BaseServiceImpl<MaterialQualityLangDao, MaterialQualityLang> implements MaterialQualityLangService {

}
