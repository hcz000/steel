package com.gv.steel.system.base.dto;

import com.gv.steel.system.base.entity.CrosscutRequire;
import com.gv.steel.system.base.entity.CustomerProfile;
import com.gv.steel.system.base.entity.RipCutRequire;
import com.gv.steel.system.base.entity.RollingRequire;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "CustomerProfileDto", description = "客户档案信息提交DTO")
public class CustomerProfileDTO extends CustomerProfile {
    private static final long serialVersionUID = 1637681989182863762L;

    @Schema(description = "纵切基本要求")
    private RipCutRequire ripCutRequire;

    @Schema(description = "横切基本要求")
    private CrosscutRequire crosscutRequire;

    @Schema(description = "压延基本要求")
    private RollingRequire rollingRequire;
}
