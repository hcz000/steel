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
 * 附件表
 * </p>
 *
 * @author administrator
 * @since 2023-11-13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_attach")
@Schema(name = "Attach对象", description = "附件表")
public class Attach extends BaseEntity<Attach> {

    private static final long serialVersionUID = 1L;

    @Schema(description = "业务主键ID")
    private Long tableId;

    @Schema(description = "业务表")
    private String tableName;

    @Schema(description = "文件ID")
    private Long fileId;

    @Schema(description = "附件名称")
    private String attachName;

    @Schema(description = "附件类型;文件后缀")
    private String type;

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
