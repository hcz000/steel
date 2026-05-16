package com.gv.steel.system.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "NoticeQueryDTO", description = "公告查询参数")
public class NoticeQueryDTO {
    @Schema(description = "公告标题")
    private String title;
    @Schema(description = "用户ID", hidden = true)
    private Long userId;
    @Schema(description = "发布状态")
    private Integer publishStatus;
}
