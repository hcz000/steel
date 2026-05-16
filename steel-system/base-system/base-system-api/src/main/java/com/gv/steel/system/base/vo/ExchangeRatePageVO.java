package com.gv.steel.system.base.vo;

import com.gv.steel.system.base.entity.ExchangeRate;
import com.gv.steel.system.base.entity.HisExchangeRate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "汇率分页VO")
public class ExchangeRatePageVO extends ExchangeRate {

    private List<HisExchangeRate> historyList;
}
