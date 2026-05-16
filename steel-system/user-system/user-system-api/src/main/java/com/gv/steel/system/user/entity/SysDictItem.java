package com.gv.steel.system.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gv.steel.common.mybatis.base.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典项
 */
@Data
@TableName("sys_dict_item")
@Schema(description = "字典项")
@EqualsAndHashCode(callSuper = true)
public class SysDictItem extends BaseEntity<SysDictItem> {

    private static final long serialVersionUID = -2706336238049426822L;
    /**
     * 所属字典类id
     */
    @Schema(description = "所属字典类id")
    private Long dictId;

    /**
     * 所属字典类id
     */
    @Schema(description = "所属字典类key")
    private String dictKey;

    /**
     * 数据值
     */
    @Schema(description = "数据值")
    private String value;

    /***
     *
     */
    @Schema(description = "语言类型")
    private String lang;

    /**
     * 标签名
     */
    @Schema(description = "标签名")
    private String label;

    /**
     * 类型
     */
    @Schema(description = "类型")
    private Integer type;

    /**
     * 描述
     */
    @Schema(description = "描述")
    private String description;

    /**
     * 排序（升序）
     */
    @Schema(description = "排序值，默认升序")
    private Integer sortOrder;

    /**
     * 备注信息
     */
    @Schema(description = "备注信息")
    private String remark;
}
