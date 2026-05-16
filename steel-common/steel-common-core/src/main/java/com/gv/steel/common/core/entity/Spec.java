package com.gv.steel.common.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 规格对象，支持接收字符串“xxx*xxx*(xxx or C or c)”
 * 允许保留两位小数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Spec implements Serializable {
    private static final long serialVersionUID = -7397291787533827950L;

    /**
     * 厚度
     */
    private BigDecimal ply;

    /**
     * 宽度
     */
    private BigDecimal width;

    /**
     * 长度
     */
    private BigDecimal length;
}
