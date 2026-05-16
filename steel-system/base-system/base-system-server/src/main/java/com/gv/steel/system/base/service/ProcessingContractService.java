package com.gv.steel.system.base.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.mybatis.base.service.BaseService;
import com.gv.steel.system.base.dto.ProcessingContractDTO;
import com.gv.steel.system.base.dto.ProcessingContractQueryDTO;
import com.gv.steel.system.base.entity.ProcessingContract;
import com.gv.steel.system.base.vo.ProcessingContractDetailVO;
import com.gv.steel.system.base.vo.ProcessingContractPageVO;

/**
 * <p>
 * 加工合同 服务类
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
public interface ProcessingContractService extends BaseService<ProcessingContract> {

    PageResult<ProcessingContractPageVO> pageProcessingContract(Page<ProcessingContractPageVO> page, ProcessingContractQueryDTO dto);

    ProcessingContractDetailVO getProcessingContractDetailById(Long id);

    ProcessingContractDetailVO getProcessingContractDetailByCustomerId(Long customerId);

    Long saveProcessContract(ProcessingContractDTO dto);

    Long updateProcessContract(ProcessingContractDTO dto);

    boolean removeProcessContractById(Long id);
}
