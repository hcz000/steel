package com.gv.steel.common.core.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 岗位信息
 */
@Data
public class SysPostInfo implements Serializable {

    private static final long serialVersionUID = -8744622014102311894L;

    private Long id;

    /**
     * 岗位编码
     */
    private String postCode;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 岗位排序
     */
    private Integer postSort;

    /**
     * 备注信息
     */
    private String remark;

}
