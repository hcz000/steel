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
import java.math.BigDecimal;

/**
 * <p>
 * 横切基本要求
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_crosscut_require")
@Schema(name = "CrosscutRequire对象", description = "横切基本要求")
public class CrosscutRequire extends BaseEntity<CrosscutRequire> {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @Schema(description = "业务表名")
    private String tableName;

    @Schema(description = "业务表ID")
    private Long tableId;

    @Schema(description = "厚度公差")
    private String thicknessTolerance;

    @Schema(description = "宽度公差(长度公差)")
    private String widthTolerance;

    @Schema(description = "硬度")
    private String hardness;

    @Schema(description = "毛刺")
    private String burr;

    @Schema(description = "边波")
    private String sideWave;

    @Schema(description = "包装件重")
    private BigDecimal packageWeight;

    @Schema(description = "对角线")
    private String diagonal;

    @Schema(description = "备注（特殊说明）")
    private String remark;

    @Schema(description = "计重方式（0-理论，1-过磅）")
    private Integer weighingWay;


    @Override
    public Serializable pkVal() {
        return this.getId();
    }

}
