package com.gv.steel.system.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(name = "TaskNoticeStatusUpdateMessageDTO", description = "任务通知状态更新消息")
public class TaskNoticeStatusUpdateMessageDTO implements Serializable {
    private static final long serialVersionUID = -8979223635214417984L;

    @Schema(description = "业务主键ID")
    private Long tableId;

    @Schema(description = "业务表名")
    private String tableName;

    @Schema(description = "接收人ID")
    private Long receiveId;

    @Schema(description = "完成时间")
    private LocalDateTime finishTime;

    @Schema(description = "审批状态，0：待审批，1：审批通过，2：审批不通过，3：作废")
    private Integer status;
}
