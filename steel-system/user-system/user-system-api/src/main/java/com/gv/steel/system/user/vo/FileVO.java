package com.gv.steel.system.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "文件明细VO")
public class FileVO implements Serializable {
    private static final long serialVersionUID = -1921592589255750214L;

    @Schema(description = "文件ID")
    private Long fileId;

    @Schema(description = "文件名")
    private String fileName;

    @Schema(description = "源文件名")
    private String originalFileName;

    @Schema(description = "文件大小KB")
    private Long fileSize;

    @Schema(description = "文件url")
    private String fileUrl;

    @Schema(description = "文件类型")
    private String type;
}
