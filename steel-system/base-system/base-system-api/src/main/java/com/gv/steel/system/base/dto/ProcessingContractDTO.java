package com.gv.steel.system.base.dto;

import com.gv.steel.system.base.entity.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "ProcessingContractDTO", description = "加工合同信息提交DTO")
public class ProcessingContractDTO extends ProcessingContract {
    private static final long serialVersionUID = -1263196811374568640L;

	@Schema(description = "工厂ID")
	private Long factoryId;

    @Schema(description = "合同附件")
    private List<Attach> attachList;

    @Schema(description = "纵切收费列表")
    private List<RipCutCharge> ripCutChargeList;

    @Schema(description = "纵切收费列表")
    private List<CrossCutCharge> crossCutChargeList;

    @Schema(description = "纵切收费列表")
    private List<RollingCharge> rollingChargeList;

    @Schema(description = "纵切收费列表")
    private List<AccessoryCharge> accessoryChargeList;
}
