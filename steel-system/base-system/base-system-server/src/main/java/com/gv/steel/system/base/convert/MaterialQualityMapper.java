package com.gv.steel.system.base.convert;

import com.gv.steel.common.core.constant.CommonConstants;
import com.gv.steel.system.base.dto.MaterialQualityDTO;
import com.gv.steel.system.base.entity.MaterialQuality;
import com.gv.steel.system.base.vo.MaterialQualityVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = CommonConstants.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MaterialQualityMapper {
    MaterialQualityVO convertVO(MaterialQuality materialQuality);

    List<MaterialQualityVO> convertVOList(List<MaterialQuality> materialQualityList);

    MaterialQuality convert(MaterialQualityDTO dto);
}
