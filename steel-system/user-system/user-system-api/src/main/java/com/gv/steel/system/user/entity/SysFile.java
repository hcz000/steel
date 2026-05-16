package com.gv.steel.system.user.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gv.steel.common.mybatis.base.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件管理
 */
@Data
@TableName("sys_file")
@Schema(description = "系统文件")
@EqualsAndHashCode(callSuper = true)
public class SysFile extends BaseEntity<SysFile> {

    private static final long serialVersionUID = 1L;

    /**
     * 文件名
     */
    @Schema(description = "文件名")
    private String fileName;

    /**
     * 原文件名
     */
    @Schema(description = "原文件名")
    private String original;

    /**
     * 容器名称
     */
    @Schema(description = "容器名称")
    private String bucketName;

    /**
     * 文件类型
     */
    @Schema(description = "文件类型")
    private String type;

    /**
     * 文件大小
     */
    @Schema(description = "文件大小")
    private Long fileSize;

    /**
     * 创建人名称
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人名称")
    private String createName;

    /**
     * 更新人名称
     */
    @Schema(description = "更新人名称")
    private String updateName;
}
