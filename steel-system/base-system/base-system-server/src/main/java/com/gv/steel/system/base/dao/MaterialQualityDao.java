package com.gv.steel.system.base.dao;

import com.gv.steel.common.mybatis.base.dao.BaseDao;
import com.gv.steel.system.base.entity.MaterialQuality;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 材质管理表 Dao 接口
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Mapper
public interface MaterialQualityDao extends BaseDao<MaterialQuality> {

    List<MaterialQuality> selectMaterialQualityList(String languageTag);
}
