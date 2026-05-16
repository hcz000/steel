package com.gv.steel.system.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(name = "TaskNoticeMessageDTO", description = "任务通知消息DTO")
public class TaskNoticeMessageDTO implements Serializable {
    private static final long serialVersionUID = -4086154613007551248L;

    @Schema(description = "业务表主键ID")
    private Long tableId;

    @Schema(description = "业务表名")
    private String tableName;

    @Schema(description = "模块名称")
    private String module;

    @Schema(description = "任务标题")
    private String title;

    @Schema(description = "任务详情页")
    private String url;

    @Schema(description = "任务参数")
    private String params;

    @Schema(description = "接收人ID")
    private Long receiveId;

    @Schema(description = "接收人名称")
    private String receiveName;

    @Schema(description = "发起人ID")
    private Long createBy;

    @Schema(description = "发起人名称")
    private String createName;
}
