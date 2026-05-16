package com.gv.steel.common.mybatis.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础字段
 *
 * @param <T>
 */
@Getter
@Setter
public abstract class CommonModel<T extends Model<T>> extends Model<T> implements Serializable {

    private static final long serialVersionUID = 1317243674671994430L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @CreatedDate
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @CreatedBy
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人")
    private Long createBy;

    @LastModifiedDate
    @TableField(fill = FieldFill.UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @LastModifiedBy
    @TableField(fill = FieldFill.UPDATE)
    @Schema(description = "更新人")
    private Long updateBy;
}
