package com.gv.steel.system.base.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(name = "ProcessAccessoryVO", description = "加工辅料VO")
public class ProcessAccessoryVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "辅料ID")
    private Long id;

    @Schema(description = "收费项")
    private String chargeItem;

    @Schema(description = "收费方式（0-辅料数量，1-成品重量，2-原卷重量）")
    private Integer chargeWay;

    @Schema(description = "单位（0-元/个，1-元/吨，2-元/千克）")
    private Integer unit;

    @Schema(description = "备注")
    private String remark;
}
