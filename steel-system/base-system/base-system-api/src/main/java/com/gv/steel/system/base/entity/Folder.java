package com.gv.steel.system.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 共享文件夹表
 * </p>
 *
 * @author administrator
 * @since 2023-11-13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_folder")
@Schema(name = "Folder对象", description = "共享文件夹表")
public class Folder extends BaseEntity<Folder> {

    private static final long serialVersionUID = 1L;

    @Schema(description = "父类ID")
    private Long parentId;

    @Schema(description = "父级目录ID组")
    private String parentIdGroup;

    @Schema(description = "父级目录ID组")
    private String parentNameGroup;

    @Schema(description = "文件夹名称")
    private String folderName;

    @Schema(description = "排序值")
    private Integer orderSort;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人")
    private String createName;


    @Override
    public Serializable pkVal() {
        return this.getId();
    }

}
