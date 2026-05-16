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
 * 发票抬头
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_invoice_title")
@Schema(name = "InvoiceTitle对象", description = "发票抬头")
public class InvoiceTitle extends BaseEntity<InvoiceTitle> {

    private static final long serialVersionUID = 1L;

    @Schema(description = "发票抬头名称")
    private String invoiceTitleName;

    @Schema(description = "公司名称")
    private String companyName;

    @Schema(description = "公司英文名称")
    private String companyNameEn;

    @Schema(description = "公司地址")
    private String companyAddress;

    @Schema(description = "公司电话")
    private String tel;

    @Schema(description = "公司传真")
    private String fax;

    @Schema(description = "提成运费;，用于提成计算中的运费项")
    private String freight;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人名称")
    private String createName;


    @Override
    public Serializable pkVal() {
        return this.getId();
    }

}
