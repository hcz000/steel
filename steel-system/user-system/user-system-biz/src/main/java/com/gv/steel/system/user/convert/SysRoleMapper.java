package com.gv.steel.system.user.convert;

import com.gv.steel.common.core.constant.CommonConstants;
import com.gv.steel.common.core.entity.SysRoleInfo;
import com.gv.steel.system.user.entity.SysRole;
import com.gv.steel.system.user.vo.RoleExcelVO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = CommonConstants.SPRING)
public interface SysRoleMapper {
    SysRoleInfo convert(SysRole sysRole);

    List<SysRoleInfo> cnvertList(List<SysRole> sysRoleList);

    RoleExcelVO convertExcelVO(SysRole sysRole);

    List<RoleExcelVO> convertExcelVOList(List<SysRole> sysRole);
}
