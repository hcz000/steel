package com.gv.steel.system.user.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 岗位excel 对应的实体
 */
@Data
public class PostExcelVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * excel 行号
     */
    private Long lineNum;

    /**
     * 主键ID
     */
    private Long postId;

    /**
     * 岗位名称
     */
    @NotBlank(message = "岗位名称不能为空")
    private String postName;

    /**
     * 岗位标识
     */
    @NotBlank(message = "岗位标识不能为空")
    private String postCode;

    /**
     * 岗位排序
     */
    @NotNull(message = "岗位排序不能为空")
    private Integer postSort;

    /**
     * 岗位描述
     */
    @NotBlank(message = "岗位描述不能为空")
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
