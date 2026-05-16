package com.gv.steel.system.base.convert;

import com.gv.steel.common.core.constant.CommonConstants;
import com.gv.steel.system.base.dto.TaskNoticeMessageDTO;
import com.gv.steel.system.base.entity.TaskNotice;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = CommonConstants.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskNoticeMapper {

    TaskNotice convert(TaskNoticeMessageDTO dto);

    List<TaskNotice> convertList(List<TaskNoticeMessageDTO> dto);
}
