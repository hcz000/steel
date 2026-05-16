package com.gv.steel.system.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gv.steel.common.mybatis.base.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 * 加工合同
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_processing_contract")
@Schema(name = "ProcessingContract对象", description = "加工合同")
public class ProcessingContract extends BaseEntity<ProcessingContract> {

    private static final long serialVersionUID = 1L;

    @Schema(description = "委托单位客户ID")
    private Long customerId;

    @Schema(description = "合同号")
    private String contractNo;

    @Schema(description = "签订日期")
    private LocalDate signedDate;

    @Schema(description = "吊装费用;元/吨")
    private BigDecimal hoistingCost;

    @Schema(description = "原卷仓储费;元/吨")
    private BigDecimal rawStorageCost;

    @Schema(description = "原卷免仓天数")
    private Integer rawFreeDays;

    @Schema(description = "成品仓储费;元/吨")
    private BigDecimal productStorageCost;

    @Schema(description = "成品免仓天数")
    private Integer productFreeDays;

    @Schema(description = "架子是否退回;0：否，1：是")
    private Integer shelfReturnFlag;

    @Schema(description = "横切收费方式;0：整单收费，1：单尺寸收费")
    private Integer crossCutChargeWay;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人名称")
    private String createName;


    @Override
    public Serializable pkVal() {
        return this.getId();
    }

}
