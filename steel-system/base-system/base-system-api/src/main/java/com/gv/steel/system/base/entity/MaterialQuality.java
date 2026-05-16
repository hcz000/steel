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
 * 材质管理表
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_material_quality")
@Schema(name = "MaterialQuality对象", description = "材质管理表")
public class MaterialQuality extends BaseEntity<MaterialQuality> {

    private static final long serialVersionUID = 1L;

    @Schema(description = "类别")
    private Integer type;

    @Schema(description = "材质名称（默认名称）")
    private String name;

    @Schema(description = "密度")
    private BigDecimal density;


    @Schema(description = "排序值")
    private Integer orderSort;

    @Override
    public Serializable pkVal() {
        return this.getId();
    }

}
