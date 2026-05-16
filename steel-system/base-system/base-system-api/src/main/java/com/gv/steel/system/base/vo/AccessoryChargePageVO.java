package com.gv.steel.system.base.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Schema(name = "AccessoryChargePageVO", description = "辅料收费分页VO")
public class AccessoryChargePageVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "加工厂ID")
    private Long factoryId;

    @Schema(description = "客户代码")
    private String customerCode;

    @Schema(description = "合同号")
    private String contractNo;

    @Schema(description = "收费项")
    private String chargeItem;

    @Schema(description = "收费方式（0-按辅料数量，1-按成品重量，2-按原卷重量）")
    private Integer chargeWay;

    @Schema(description = "单位（0：元/个，1：元/千克，2：元/吨）")
    private Integer unit;

    @Schema(description = "单价")
    private BigDecimal unitPrice;

    @Schema(description = "使用范围;0：整单适用，1：单件适用，2：捆包适用")
    private Integer useScope;

    @Schema(description = "备注")
    private String remark;
}
