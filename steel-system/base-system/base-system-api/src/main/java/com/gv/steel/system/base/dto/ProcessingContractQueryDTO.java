package com.gv.steel.system.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Schema(name = "ProcessingContractQueryDTO", description = "加工合同查询DTO")
public class ProcessingContractQueryDTO implements Serializable {
    private static final long serialVersionUID = -8723107632767809761L;

    @Schema(description = "委托客户ID")
    private Long customerId;

    @Schema(description = "合同号")
    private String contractNo;

    @Schema(description = "签订日期范围, “yyyy-MM-dd,yyyy-MM-dd”")
    private LocalDate[] signedDate;
}
