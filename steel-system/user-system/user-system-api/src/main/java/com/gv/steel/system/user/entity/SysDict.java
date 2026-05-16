package com.gv.steel.system.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gv.steel.common.mybatis.base.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典表
 */
@Data
@TableName("sys_dict")
@Schema(description = "字典类型")
@EqualsAndHashCode(callSuper = true)
public class SysDict extends BaseEntity<SysDict> {

    private static final long serialVersionUID = 1L;

    /**
     * 类型
     */
    @Schema(description = "字典key")
    private String dictKey;

    /**
     * 描述
     */
    @Schema(description = "字典描述")
    private String description;

    /**
     * 是否是系统内置
     */
    @Schema(description = "是否系统内置")
    private Integer systemFlag;

    /**
     * 备注信息
     */
    @Schema(description = "备注信息")
    private String remark;
}
