package com.gv.steel.system.base.dto.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CustomerRequireImportDTO implements Serializable {
    private static final long serialVersionUID = -3837690379745956106L;

    private String ownerCode;

    /*************ft********************/

    @ExcelProperty(value = "ft_houdu_gongcha")
    private String houdugongcha;

    @ExcelProperty(value = "ft_kuandu_gongcha")
    private String kuandugongcha;

    @ExcelProperty(value = "ft_yingdu")
    private String yingdu;

    @ExcelProperty(value = "ft_maoci")
    private String maoci;

    @ExcelProperty(value = "ft_wanqudu")
    private String wanqudu;

    @ExcelProperty(value = "ft_baozhuang_zhong")
    private BigDecimal baozhuangzhong;

    @ExcelProperty(value = "ft_zuixiao_neijing")
    private BigDecimal zuixiaoneijing;

    @ExcelProperty(value = "ft_zuida_waijing")
    private BigDecimal zuidawaijing;

    @ExcelProperty(value = "ft_jiagong_yaoqiu")
    private String jiagongyaoqiu;

    /**************jb***************/

    @ExcelProperty(value = "jb_houdu_gongcha")
    private String jbhoudugongcha;

    @ExcelProperty(value = "jb_changdu_gongcha")
    private String jbchangdugongcha;

    @ExcelProperty(value = "jb_bianbo")
    private String bianbo;

    @ExcelProperty(value = "jb_yingdu")
    private String jbyingdu;

    @ExcelProperty(value = "jb_maoci")
    private String jbmaoci;

    @ExcelProperty(value = "jb_baozhuang_zhong")
    private BigDecimal jbbaozhuangzhong;

    @ExcelProperty(value = "jb_jiagong_yaoqiu")
    private String jbjiagongyaoqiu;
}
