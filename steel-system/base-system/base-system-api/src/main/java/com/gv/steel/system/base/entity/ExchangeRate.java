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
 * 汇率管理表
 * </p>
 *
 * @author administrator
 * @since 2023-11-03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_exchange_rate")
@Schema(name = "ExchangeRate对象", description = "汇率管理表")
public class ExchangeRate extends BaseEntity<ExchangeRate> {

    private static final long serialVersionUID = 1L;

    @Schema(description = "币种名称对")
    private String targetShortName;

    @Schema(description = "币种简称对")
    private String sourceShortName;

    @Schema(description = "汇率")
    private BigDecimal exchangeRate;

    @Schema(description = "原币名称")
    private String sourceName;

    @Schema(description = "目标币名称")
    private String targetName;


    @Override
    public Serializable pkVal() {
        return this.getId();
    }

}
