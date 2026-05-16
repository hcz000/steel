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
 * 材质语言表
 * </p>
 *
 * @author administrator
 * @since 2023-11-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_material_quality_lang")
@Schema(name = "MaterialQualityLang对象", description = "材质语言表")
public class MaterialQualityLang extends BaseEntity<MaterialQualityLang> {

    private static final long serialVersionUID = 1L;

    @Schema(description = "材质id")
    private Long materialQualityId;

    @Schema(description = "语言")
    private String lang;

    @Schema(description = "名称")
    private String label;


    @Override
    public Serializable pkVal() {
        return this.getId();
    }

}
