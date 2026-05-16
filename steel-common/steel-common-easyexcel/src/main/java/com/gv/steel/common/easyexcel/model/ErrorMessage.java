package com.gv.steel.common.easyexcel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Excel导入异常信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {
    /**
     * excel error line num
     */
    private Long lineNum;
    /**
     * excel msg
     */
    private Set<String> errors = new HashSet<>();
}
