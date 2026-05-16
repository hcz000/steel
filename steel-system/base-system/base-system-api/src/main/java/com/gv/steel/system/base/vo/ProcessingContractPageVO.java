package com.gv.steel.system.base.vo;

import com.gv.steel.system.base.entity.ProcessingContract;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "ProcessingContractPageVO", description = "加工合同分页查询VO")
public class ProcessingContractPageVO extends ProcessingContract {
    private static final long serialVersionUID = -3641190941009384425L;

    /**
     * 委托单位名称
     */
    @Schema(description = "委托单位名称")
    private String customerName;


}
