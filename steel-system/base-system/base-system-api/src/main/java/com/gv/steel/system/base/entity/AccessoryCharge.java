package com.gv.steel.system.base.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gv.steel.common.mybatis.base.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 辅料收费
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_accessory_charge")
@Schema(name = "AccessoryCharge对象", description = "辅料收费")
public class AccessoryCharge extends BaseEntity<AccessoryCharge> {

    private static final long serialVersionUID = 1L;

    @Schema(description = "排序值(0开始)")
    private Integer orderSort;

    @Schema(description = "合同ID")
    private Long contractId;

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


    @Override
    public Serializable pkVal() {
        return this.getId();
    }

}
