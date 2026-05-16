package com.gv.steel.system.base.convert;

import com.gv.steel.common.core.constant.CommonConstants;
import com.gv.steel.system.base.dto.ProcessingContractDTO;
import com.gv.steel.system.base.entity.ProcessingContract;
import com.gv.steel.system.base.vo.ProcessingContractDetailVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = CommonConstants.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProcessingContractMapper {
    ProcessingContractDetailVO convertVO(ProcessingContract processingContract);

    ProcessingContract convert(ProcessingContractDTO dto);
}
