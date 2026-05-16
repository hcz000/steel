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
 * 通知公告
 * </p>
 *
 * @author administrator
 * @since 2023-11-13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_notice")
@Schema(name = "Notice对象", description = "通知公告")
public class Notice extends BaseEntity<Notice> {

    private static final long serialVersionUID = 1L;

    @Schema(description = "公告标题")
    private String title;

    @Schema(description = "具体内容")
    private String content;

    @Schema(description = "排序值")
    private Integer orderSort;

    @Schema(description = "发布状态，0未发布，1已发布")
    private Integer publishStatus;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "发起人")
    private String createName;

    @Schema(description = "已读状态，0未读，1已读")
    @TableField(exist = false)
    private Integer readFlag;


    @Override
    public Serializable pkVal() {
        return this.getId();
    }

}
