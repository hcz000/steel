package com.gv.steel.common.mybatis.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 版本控制和删除标识
 *
 * @param <T>
 */
@Getter
@Setter
public class BaseEntity<T extends Model<T>> extends CommonModel<T> {
    private static final long serialVersionUID = -6446915741052668715L;

    @JsonIgnore
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "删除标识", hidden = true)
    private Integer deleteFlag;


    @Version
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "版本控制")
    private Integer version;
}
