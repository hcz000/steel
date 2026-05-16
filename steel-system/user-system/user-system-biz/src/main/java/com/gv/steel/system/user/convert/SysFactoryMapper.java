package com.gv.steel.system.user.convert;

import com.gv.steel.common.core.constant.CommonConstants;
import com.gv.steel.common.core.entity.SysFactoryInfo;
import com.gv.steel.system.user.entity.SysFactory;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = CommonConstants.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysFactoryMapper {
    SysFactoryInfo convertInfo(SysFactory sysFactory);

    List<SysFactoryInfo> cnvertInfoList(List<SysFactory> factoryList);
}
