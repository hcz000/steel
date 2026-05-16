package com.gv.steel.system.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(name = "AccessoryChargeQueryDTO", description = "辅料收费查询DTO")
public class AccessoryChargeQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "加工厂ID")
    private Long factoryId;

    @Schema(description = "客户ID")
    private Long customerId;

    @Schema(description = "合同号")
    private String contractNo;

    @Schema(description = "收费项")
    private String chargeItem;

    @Schema(description = "收费方式（0-按辅料数量，1-按成品重量，2-按原卷重量）")
    private Integer chargeWay;

    @Schema(description = "使用范围;0：整单适用，1：单件适用，2：捆包适用")
    private Integer useScope;
}
