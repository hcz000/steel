package com.gv.steel.system.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Schema(name = "CrossCutChargeQueryDTO", description = "横切加工费查询DTO")
public class CrossCutChargeQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "加工厂ID")
    private Long factoryId;

    @Schema(description = "客户ID")
    private Long customerId;

    @Schema(description = "合同号")
    private String contractNo;

    @Schema(description = "材质ID")
    private Long materialQualityId;

    @Schema(description = "原卷厚度")
    private BigDecimal rawMaterialPly;

    @Schema(description = "成品宽度")
    private BigDecimal productWidth;

    @Schema(description = "成品长度")
    private BigDecimal productLength;

}
