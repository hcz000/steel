package com.gv.steel.system.base.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(name = "CustomerProfileSelectItemVO", description = "客户档案选择项VO")
public class CustomerProfileSelectItemVO implements Serializable {
    private static final long serialVersionUID = -5944287530291117484L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "父级ID")
    private Long parentId;

    @Schema(description = "工厂ID")
    private Long factoryId;

    @Schema(description = "客户类型;0：委托客户，1：贸易客户")
    private Integer customerType;

    @Schema(description = "客户代码")
    private String customerCode;

    @Schema(description = "客户名称")
    private String customerName;

    @Schema(description = "是否打印库存;0：否，1：是")
    private Integer printStorageFlag;

    @Schema(description = "打印抬头")
    private String labelTitle;

    @Schema(description = "付款期限")
    private Integer paymentTerms;
}
