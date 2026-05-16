package com.gv.steel.system.user.convert;

import com.gv.steel.common.core.constant.CommonConstants;
import com.gv.steel.common.core.vo.UserInfo;
import com.gv.steel.system.user.dto.UserDTO;
import com.gv.steel.system.user.entity.SysUser;
import com.gv.steel.system.user.vo.UserExcelVO;
import com.gv.steel.system.user.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = CommonConstants.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysUserMapper {
    @Mapping(target = "userId", source = "id")
    UserInfo convertUserInfo(SysUser sysUser);

    SysUser convert(UserDTO userDTO);

    UserExcelVO convertVO2Excel(UserVO userVO);
}
