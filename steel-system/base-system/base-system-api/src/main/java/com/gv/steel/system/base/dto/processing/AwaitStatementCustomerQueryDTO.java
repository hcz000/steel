package com.gv.steel.system.base.dto.processing;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(name = "AwaitStatementQueryDTO", description = "带对账的客户查询DTO")
public class AwaitStatementCustomerQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "加工厂ID")
    private Long factoryId;

    @Schema(description = "客户代码")
    private String customerCode;

    @Schema(description = "客户代码集合")
    private Long[] customerIds;
}
