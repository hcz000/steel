package com.gv.steel.system.base.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 历史汇率表
 * </p>
 *
 * @author administrator
 * @since 2024-02-05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_his_exchange_rate")
@EqualsAndHashCode(callSuper = true)
@Schema(name = "HisExchangeRate对象", description = "历史汇率表")
public class HisExchangeRate extends Model<HisExchangeRate> {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "汇率ID")
    private Long nid;

    @Schema(description = "币种名称对")
    private String targetShortName;

    @Schema(description = "币种简称对")
    private String sourceShortName;

    @Schema(description = "目标币名称")
    private String targetName;

    @Schema(description = "原币名称")
    private String sourceName;

    @Schema(description = "汇率")
    private BigDecimal exchangeRate;

    @CreatedDate
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @CreatedBy
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人")
    private Long createBy;

    @LastModifiedDate
    @TableField(fill = FieldFill.UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @LastModifiedBy
    @TableField(fill = FieldFill.UPDATE)
    @Schema(description = "更新人")
    private Long updateBy;

    @Override
    public Serializable pkVal() {
        return this.getId();
    }

}
