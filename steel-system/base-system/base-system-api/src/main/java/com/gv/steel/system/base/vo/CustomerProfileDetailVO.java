package com.gv.steel.system.base.vo;

import com.gv.steel.system.base.entity.CrosscutRequire;
import com.gv.steel.system.base.entity.CustomerProfile;
import com.gv.steel.system.base.entity.RipCutRequire;
import com.gv.steel.system.base.entity.RollingRequire;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "CustomerProfileDetailVO", description = "客户档案明细VO")
public class CustomerProfileDetailVO extends CustomerProfile {
    private static final long serialVersionUID = 2870019766438997764L;

    @Schema(description = "纵切基本要求")
    private RipCutRequire ripCutRequire;

    @Schema(description = "横切基本要求")
    private CrosscutRequire crosscutRequire;

    @Schema(description = "压延基本要求")
    private RollingRequire rollingRequire;
}
