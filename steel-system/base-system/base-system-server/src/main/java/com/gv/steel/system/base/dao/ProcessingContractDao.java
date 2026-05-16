package com.gv.steel.system.base.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.mybatis.base.dao.BaseDao;
import com.gv.steel.system.base.dto.ProcessingContractQueryDTO;
import com.gv.steel.system.base.entity.ProcessingContract;
import com.gv.steel.system.base.vo.ProcessingContractPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 加工合同 Dao 接口
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Mapper
public interface ProcessingContractDao extends BaseDao<ProcessingContract> {

    Page<ProcessingContractPageVO> selectProcessingContractPage(Page<ProcessingContractPageVO> page, @Param("query") ProcessingContractQueryDTO dto);
}
