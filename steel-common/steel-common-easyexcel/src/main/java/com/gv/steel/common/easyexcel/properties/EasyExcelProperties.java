package com.gv.steel.common.easyexcel.properties;

import com.gv.steel.common.easyexcel.constants.EasyExcelConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * easy excel 配置
 */
@Data
@ConfigurationProperties(prefix = EasyExcelConstants.PROPERTIES_PREFIX)
public class EasyExcelProperties {
    /**
     * 模板文件所在位置
     */
    private String templatePath = "excel";
}
