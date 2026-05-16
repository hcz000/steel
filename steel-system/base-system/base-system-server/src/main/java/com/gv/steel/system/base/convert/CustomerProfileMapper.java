package com.gv.steel.system.base.convert;

import com.gv.steel.common.core.constant.CommonConstants;
import com.gv.steel.system.base.dto.CustomerProfileDTO;
import com.gv.steel.system.base.entity.CustomerProfile;
import com.gv.steel.system.base.vo.CustomerProfileDetailVO;
import com.gv.steel.system.base.vo.CustomerProfileSelectItemVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = CommonConstants.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerProfileMapper {
    CustomerProfileDetailVO convertVO(CustomerProfile customerProfile);

    CustomerProfile convert(CustomerProfileDTO dto);

    CustomerProfileSelectItemVO convertSelectItemVo(CustomerProfile customerProfile);

    List<CustomerProfileSelectItemVO> convertSelectItemVoList(List<CustomerProfile> list);
}
