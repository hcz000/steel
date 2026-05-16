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
 * 横切收费
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "t_cross_cut_charge", autoResultMap = true)
@Schema(name = "CrossCutCharge对象", description = "横切收费")
public class CrossCutCharge extends BaseEntity<CrossCutCharge> {

    private static final long serialVersionUID = 1L;

    @Schema(description = "合同ID")
    private Long contractId;

    @TableField(typeHandler = ArrayTypeHandler.class)
    @Schema(description = "材质ID;ID集合")
    private Long[] materialId;

    @TableField(typeHandler = ArrayTypeHandler.class)
    @Schema(description = "材质名称;名称集合字符串，以,分隔")
    private String[] materialName;

    @Schema(description = "原卷厚度1;单位：mm")
    private BigDecimal materialPly1;

    @Schema(description = "原卷厚度2;单位：mm")
    private BigDecimal materialPly2;

    @Schema(description = "成品宽度1;单位：mm")
    private BigDecimal productWidth1;

    @Schema(description = "成品宽度2;单位：mm")
    private BigDecimal productWidth2;

    @Schema(description = "成品长度1;单位：mm")
    private BigDecimal productLength1;

    @Schema(description = "成品长度2;单位：mm")
    private BigDecimal productLength2;

    @Schema(description = "最低收费;重量不足按最低收费计算")
    private BigDecimal minimumCharge;

    @Schema(description = "最小重量;单位：吨")
    private BigDecimal minimumWeight;

    @Schema(description = "单价;单位：元/吨")
    private BigDecimal unitPrice;

    @Schema(description = "再加工单价")
    private BigDecimal reprocessUnitPrice;

    @Schema(description = "备注")
    private String remark;


    @Override
    public Serializable pkVal() {
        return this.getId();
    }

}
