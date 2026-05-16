package com.gv.steel.system.base.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.mybatis.base.dao.BaseDao;
import com.gv.steel.system.base.dto.CrossCutChargeQueryDTO;
import com.gv.steel.system.base.entity.CrossCutCharge;
import com.gv.steel.system.base.vo.CrossCutChargePageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 横切收费 Dao 接口
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Mapper
public interface CrossCutChargeDao extends BaseDao<CrossCutCharge> {

    Page<CrossCutChargePageVO> selectCrossCutPage(Page<CrossCutChargePageVO> page, @Param("param") CrossCutChargeQueryDTO dto);
}
