package com.gv.steel.system.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class ExchangeRateQueryDTO implements Serializable {

    private static final long serialVersionUID = -7416722418864706188L;

    @Schema(description = "币种名称对")
    private String targetShortName;

    @Schema(description = "币种简称对")
    private String sourceShortName;

    private LocalDate[] timeZone;

}
