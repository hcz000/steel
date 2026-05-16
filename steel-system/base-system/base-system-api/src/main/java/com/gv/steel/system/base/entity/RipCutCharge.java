package com.gv.steel.system.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gv.steel.common.mybatis.base.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.ArrayTypeHandler;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 纵切收费
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "t_rip_cut_charge", autoResultMap = true)
@Schema(name = "RipCutCharge对象", description = "纵切收费")
public class RipCutCharge extends BaseEntity<RipCutCharge> {

    private static final long serialVersionUID = 1L;

    @Schema(description = "合同ID")
    private Long contractId;

    @Schema(description = "纵切收费类型（0-纵切，1-重卷）")
    private Integer chargeType;

    @TableField(typeHandler = ArrayTypeHandler.class)
    @Schema(description = "材质ID;ID集合字符串，以,分隔")
    private Long[] materialId;

    @TableField(typeHandler = ArrayTypeHandler.class)
    @Schema(description = "材质名称;名称集合字符串，以,分隔")
    private String[] materialName;

    @Schema(description = "原卷厚度1;单位：mm")
    private BigDecimal materialPly1;

    @Schema(description = "原卷厚度2;单位：mm")
    private BigDecimal materialPly2;

    @Schema(description = "原卷宽度1;单位：mm")
    private BigDecimal materialWidth1;

    @Schema(description = "原卷宽度2;单位：mm")
    private BigDecimal materialWidth2;

    @Schema(description = "最低收费;重量不足按最低收费计算")
    private BigDecimal minimumCharge;

    @Schema(description = "最小重量;单位：吨")
    private BigDecimal minimumWeight;

    @Schema(description = "单价;单位：元/吨")
    private BigDecimal unitPrice;

    @Schema(description = "最大条数;超出条数按")
    private Integer maximumRow;

    @Schema(description = "加收方式;0：按条数，1：按比例")
    private Integer addedChargeWay;

    @Schema(description = "加收值;加收值")
    private BigDecimal addedChargeValue;

    @Schema(description = "再加工单价")
    private BigDecimal reprocessUnitPrice;

    @Schema(description = "备注")
    private String remark;


    @Override
    public Serializable pkVal() {
        return this.getId();
    }

}
