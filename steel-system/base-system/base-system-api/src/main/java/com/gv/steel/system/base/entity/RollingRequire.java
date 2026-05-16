package com.gv.steel.system.base.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gv.steel.common.mybatis.base.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 压延基本要求
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_rolling_require")
@Schema(name = "RollingRequire对象", description = "压延基本要求")
public class RollingRequire extends BaseEntity<RollingRequire> {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @Schema(description = "业务表名")
    private String tableName;

    @Schema(description = "业务表ID")
    private Long tableId;

    @Schema(description = "材质")
    private String materialQuality;

    @Schema(description = "厚度公差")
    private String thicknessTolerance;

    @Schema(description = "表面等级")
    private String surfaceGrade;

    @Schema(description = "精整后硬度")
    private String hardnessAfterFinish;

    @Schema(description = "球化组织")
    private String spheroidizedStructure;

    @Schema(description = "延伸率")
    private String elongationRate;

    @Schema(description = "是否精整（0-否，1-是）")
    private Integer finish;

    @Schema(description = "备注（特殊说明）")
    private String remark;

    @Schema(description = "拉伸等级（MPA）")
    private String ts;

    @Schema(description = "屈服等级（MPA）")
    private String ys;

    @Schema(description = "预期损耗（%）")
    private String el;

    @Schema(description = "硬度")
    private String hardness;


    @Override
    public Serializable pkVal() {
        return this.getId();
    }

}
