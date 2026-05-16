package com.gv.steel.system.base.vo;

import com.gv.steel.system.base.entity.CustomerProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "CustomerProfilePageVO", description = "客户档案分页查询VO")
public class CustomerProfilePageVO extends CustomerProfile {
    private static final long serialVersionUID = -7787107002875470763L;

    @Schema(description = "委托单位名称")
    private String delegateCustomerName;

    @Schema(description = "贸易客户")
    private String tradeCustomerName;

    @Schema(description = "加工合同ID")
    private Long processContractId;
}

