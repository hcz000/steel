package com.gv.steel.system.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gv.steel.common.mybatis.base.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.ArrayTypeHandler;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 客户档案（委托单位、贸易客户）
 * </p>
 *
 * @author administrator
 * @since 2023-11-08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "t_customer_profile", autoResultMap = true)
@Schema(name = "CustomerProfile对象", description = "客户档案（委托单位、贸易客户）")
public class CustomerProfile extends BaseEntity<CustomerProfile> {

    private static final long serialVersionUID = 1L;

    @Schema(description = "父级ID;委托单位父级ID为0，贸易单位父级ID为委托单位ID")
    private Long parentId;

    @Schema(description = "工厂ID;所属工厂")
    private Long factoryId;

    @Schema(description = "工厂名称;所属工厂")
    private String factoryName;

    @Schema(description = "客户类型;0：委托客户，1：贸易客户")
    private Integer customerType;

    @Schema(description = "客户代码")
    private String customerCode;

    @Schema(description = "客户名称")
    private String customerName;

    @Schema(description = "联系电话")
    private String tel;

    @Schema(description = "传真")
    private String fax;

    @Schema(description = "联系人")
    private String linkMan;

    @Schema(description = "月结方式;0：5日结算，1：10日结算，2：15日结算，3：20日结算，4：25日结算，5：月底结算")
    private Integer monthlyStatementWay;

    @Schema(description = "出货单类型")
    private Long deliveryCategoryId;

    @Schema(description = "付款期限;0：延期一个月，1：延期两个月，2：延期三个月，3：延期六个月")
    private Integer paymentTerms;

    @Schema(description = "业务员ID")
    private Long salesmanId;

    @Schema(description = "业务员名称")
    private String salesmanName;

    @Schema(description = "业务类型;0：自行开发，1：业务跟单")
    private Integer businessType;

    @Schema(description = "加工费计重方式;0：原卷计算，1：出货计算")
    private Integer processingCostWeighingWay;

    @Schema(description = "币种简称")
    private String currency;

    @Schema(description = "是否含税;0：不含税，1：含税")
    private Integer taxInclusiveFlag;

    @Schema(description = "税率")
    private BigDecimal taxRete;

    @Schema(description = "是否打印库存;0：否，1：是")
    private Integer printStorageFlag;

    @Schema(description = "打印抬头")
    private String labelTitle;

    @Schema(description = "出货抬头")
    private String sellTitle;

    @TableField(typeHandler = ArrayTypeHandler.class)
    @Schema(description = "原卷标签ID")
    private Long[] rawMaterialLabelId;

    @TableField(typeHandler = ArrayTypeHandler.class)
    @Schema(description = "板料成品标签ID")
    private Long[] sheetProductLabelId;

    @TableField(typeHandler = ArrayTypeHandler.class)
    @Schema(description = "条料成品标签ID")
    private Long[] stripProductLabelId;

    @TableField(typeHandler = ArrayTypeHandler.class)
    @Schema(description = "单条成品标签ID")
    private Long[] singleStripProductLabelId;

    @TableField(typeHandler = ArrayTypeHandler.class)
    @Schema(description = "出货单标签ID")
    private Long[] deliveryLabelId;


    @Override
    public Serializable pkVal() {
        return this.getId();
    }

}
