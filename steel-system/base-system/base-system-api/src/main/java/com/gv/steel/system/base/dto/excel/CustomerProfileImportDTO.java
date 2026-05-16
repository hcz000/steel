package com.gv.steel.system.base.dto.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Schema(name = "CustomerProfileImportDTO", description = "客户档案导入DTO")
public class CustomerProfileImportDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "客户代码", index = 0)
    private String customerCode;

    @ExcelProperty(value = "客户名称", index = 1)
    private String customerName;

    @ExcelProperty(value = "月结方式", index = 2)
    private String monthlySettlementWayDesc;

    @ExcelProperty(value = "吊装费", index = 3)
    private BigDecimal hoistingCost;

    @ExcelProperty(value = "原卷仓储费", index = 4)
    private BigDecimal rawStorageCost;

    @ExcelProperty(value = "原卷免仓天数", index = 5)
    private Integer rawFreeDays;

    @ExcelProperty(value = "加工费计重方式", index = 6)
    private String processingCostWeighingWayDesc;

    @ExcelProperty(value = "是否打印库存", index = 11)
    private String printStorageFlagDesc;

    @ExcelProperty(value = "是否含税", index = 14)
    private String taxInclusiveFlagDesc;

    @ExcelProperty(value = "出货抬头", index = 12)
    private String rise;
}
