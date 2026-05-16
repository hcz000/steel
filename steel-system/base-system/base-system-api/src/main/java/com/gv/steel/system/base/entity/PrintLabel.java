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
 * 打印标签
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_print_label")
@Schema(name = "PrintLabel对象", description = "打印标签")
public class PrintLabel extends BaseEntity<PrintLabel> {

    private static final long serialVersionUID = 1L;

    @Schema(description = "标签名")
    private String labelName;

    @Schema(description = "标签code值;，用于打印控件")
    private String labelCode;

    @Schema(description = "标签类型，(0-原卷标签，1-条料标签，2-小条料标签，3-板料标签，4-半成品标签,5-其他模板)")
    private Integer labelType;

    @Schema(description = "标签模板文件ID")
    private Long labelFileId;

    @Schema(description = "标签模板名称")
    private String labelFileName;

    @Schema(description = "排序值")
    private Integer orderSort;

    @Schema(description = "缩略图path")
    private String thumbPath;

    @Schema(description = "缩略图附件ID")
    private Long thumbFileId;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人名称")
    private String createName;


    @Override
    public Serializable pkVal() {
        return this.getId();
    }

}
