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
 * 共享文件表
 * </p>
 *
 * @author administrator
 * @since 2023-11-13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_share_file")
@Schema(name = "ShareFile对象", description = "共享文件表")
public class ShareFile extends BaseEntity<ShareFile> {

    private static final long serialVersionUID = 1L;

    @Schema(description = "文件ID")
    private Long fileId;

    @Schema(description = "文件夹ID")
    private Long folderId;

    @Schema(description = "目录ID组")
    private String folderIdGroup;

    @Schema(description = "目录ID组")
    private String folderNameGroup;

    @Schema(description = "附件名称")
    private String attachName;

    @Schema(description = "附件后缀")
    private String attachType;

    @Schema(description = "附件大小")
    private Long attachSize;

    @Schema(description = "备注")
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人名称")
    private String createName;


    @Override
    public Serializable pkVal() {
        return this.getId();
    }

}
