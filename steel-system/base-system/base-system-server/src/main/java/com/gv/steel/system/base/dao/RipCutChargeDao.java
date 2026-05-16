package com.gv.steel.system.base.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.mybatis.base.dao.BaseDao;
import com.gv.steel.system.base.dto.RipCutChargeQueryDTO;
import com.gv.steel.system.base.entity.RipCutCharge;
import com.gv.steel.system.base.vo.RipCutChargePageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 纵切收费 Dao 接口
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Mapper
public interface RipCutChargeDao extends BaseDao<RipCutCharge> {

    Page<RipCutChargePageVO> selectRipCutChargePage(Page<RipCutChargePageVO> page, @Param("param") RipCutChargeQueryDTO dto);
}
