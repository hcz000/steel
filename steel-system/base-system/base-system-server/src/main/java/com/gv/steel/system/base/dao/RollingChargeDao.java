package com.gv.steel.system.base.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.mybatis.base.dao.BaseDao;
import com.gv.steel.system.base.dto.RollingChargeQueryDTO;
import com.gv.steel.system.base.entity.RollingCharge;
import com.gv.steel.system.base.vo.RollingChargePageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 压延收费 Dao 接口
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Mapper
public interface RollingChargeDao extends BaseDao<RollingCharge> {

    Page<RollingChargePageVO> selectRollingChargePage(Page<RollingChargePageVO> page, @Param("param") RollingChargeQueryDTO dto);
}
