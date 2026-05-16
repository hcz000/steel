package com.gv.steel.system.base.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(name = "FolderFileVO", description = "文件夹文件VO")
public class FolderFileVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "类型; 0:文件夹,1:文件")
    private Integer type;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "文件ID")
    private Long fileId;

    @Schema(description = "文件后缀")
    private String fileType;

    @Schema(description = "文件大小")
    private Long fileSize;

    @Schema(description = "父级目录ID")
    private String parentIds;

    @Schema(description = "父级目录名称")
    private String parentNames;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建人")
    private String createName;
}
