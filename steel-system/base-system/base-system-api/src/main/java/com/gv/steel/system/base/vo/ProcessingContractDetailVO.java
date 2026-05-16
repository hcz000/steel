package com.gv.steel.system.base.vo;

import com.google.common.collect.Lists;
import com.gv.steel.system.base.entity.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "ProcessingContractDetailVO", description = "加工合同明细VO")
public class ProcessingContractDetailVO extends ProcessingContract {
    private static final long serialVersionUID = 4206748616592779899L;

	@Schema(description = "工厂ID")
	private Long factoryId;

    @Schema(description = "合同附件")
    private List<Attach> attachList = Lists.newArrayList();

    @Schema(description = "纵切收费列表")
    private List<RipCutCharge> ripCutChargeList = Lists.newArrayList();

    @Schema(description = "纵切收费列表")
    private List<CrossCutCharge> crossCutChargeList = Lists.newArrayList();

    @Schema(description = "纵切收费列表")
    private List<RollingCharge> rollingChargeList = Lists.newArrayList();

    @Schema(description = "纵切收费列表")
    private List<AccessoryCharge> accessoryChargeList = Lists.newArrayList();
}
