package com.gv.steel.system.base.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Schema(name = "RollingChargePageVO", description = "压延收费分页VO")
public class RollingChargePageVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "加工厂ID")
    private Long factoryId;

    @Schema(description = "客户代码")
    private String customerCode;

    @Schema(description = "合同号")
    private String contractNo;

    @Schema(description = "材质ID;ID集合")
    private Long[] materialId;

    @Schema(description = "材质名称;名称集合")
    private String[] materialName;

    @Schema(description = "型号;名称集合")
    private String[] model;

    @Schema(description = "原卷厚度1;单位：mm")
    private BigDecimal materialPly1;

    @Schema(description = "原卷厚度2;单位：mm")
    private BigDecimal materialPly2;

    @Schema(description = "成品厚度1;单位：mm")
    private BigDecimal productPly1;

    @Schema(description = "成品厚度2;单位：mm")
    private BigDecimal productPly2;

    @Schema(description = "最低收费;重量不足按最低收费计算")
    private BigDecimal minimumCharge;

    @Schema(description = "最小重量;单位：吨")
    private BigDecimal minimumWeight;

    @Schema(description = "单价;单位：元/吨")
    private BigDecimal unitPrice;
}
