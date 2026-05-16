package com.gv.steel.system.base.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gv.steel.common.mybatis.base.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 币种管理表
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_currency")
@Schema(name = "Currency对象", description = "币种管理表")
public class Currency extends BaseEntity<Currency> {

    private static final long serialVersionUID = 1L;

    @Schema(description = "币种名称")
    private String name;

    @Schema(description = "币种简称")
    private String shortName;

    @Schema(description = "币种")
    private String symbol;

    @Schema(description = "排序值（备用）")
    private Integer sortOrder;


    @Override
    public Serializable pkVal() {
        return this.getId();
    }

}
