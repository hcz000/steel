package com.gv.steel.system.base.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.mybatis.base.dao.BaseDao;
import com.gv.steel.system.base.dto.AccessoryChargeQueryDTO;
import com.gv.steel.system.base.entity.AccessoryCharge;
import com.gv.steel.system.base.vo.AccessoryChargePageVO;
import com.gv.steel.system.base.vo.ProcessAccessoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 辅料收费 Dao 接口
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Mapper
public interface AccessoryChargeDao extends BaseDao<AccessoryCharge> {

    List<ProcessAccessoryVO> selectByCustomerIdAndUseScope(@Param("customerId") Long customerId, @Param("useScope") Integer useScope);

    Page<AccessoryChargePageVO> selectAccessoryChargePage(Page<AccessoryChargePageVO> page, @Param("param") AccessoryChargeQueryDTO dto);
}
