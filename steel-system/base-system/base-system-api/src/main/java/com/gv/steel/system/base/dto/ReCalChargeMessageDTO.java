package com.gv.steel.system.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(name = "ReCalChargeMessageDTO", description = "重新计算加工费MQ消息DTO")
public class ReCalChargeMessageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "客户ID")
    private Long customerId;
}
