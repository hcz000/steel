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
 * 出货单类型
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_delivery_category")
@Schema(name = "DeliveryCategory对象", description = "出货单类型")
public class DeliveryCategory extends BaseEntity<DeliveryCategory> {

    private static final long serialVersionUID = 1L;

    @Schema(description = "出货单名称")
    private String deliveryOrderName;

    @Schema(description = "出货单code;，用于打印控件")
    private String deliveryOrderCode;

    @Schema(description = "缩略图path")
    private String thumbPath;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人名称")
    private String createName;


    @Override
    public Serializable pkVal() {
        return this.getId();
    }

}
