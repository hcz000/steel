package com.gv.steel.system.user.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.gv.steel.common.easyexcel.annotation.ExcelLine;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 用户excel 对应的实体
 */
@Data
@ColumnWidth(30)
public class UserExcelVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * excel 行号
     */
    @ExcelLine
    @ExcelIgnore
    private Long lineNum;

    /**
     * 主键ID
     */
    @ExcelProperty("用户编号")
    private Long userId;

    /**
     * 用户名
     */
    @ExcelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 手机号
     */
    @ExcelProperty("手机号")
    private String phone;

    /**
     * 部门名称
     */
    @ExcelProperty("部门名称")
    @NotBlank(message = "部门名称不能为空")
    private String deptName;

    /**
     * 角色列表
     */
    @ExcelProperty("角色")
    @NotBlank(message = "角色不能为空")
    private String roleNameList;

    /**
     * 所属工厂
     */
    @ExcelProperty("所属工厂")
    @NotBlank(message = "所属工厂不能为空")
    private String factoryNameList;
}
