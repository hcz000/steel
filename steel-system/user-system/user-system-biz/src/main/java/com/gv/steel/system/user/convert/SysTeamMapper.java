package com.gv.steel.system.user.convert;

import com.gv.steel.common.core.constant.CommonConstants;
import com.gv.steel.common.core.entity.SysTeamInfo;
import com.gv.steel.system.user.entity.SysTeam;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = CommonConstants.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysTeamMapper {
    List<SysTeamInfo> convertTempInfoList(List<SysTeam> teamList);
}
