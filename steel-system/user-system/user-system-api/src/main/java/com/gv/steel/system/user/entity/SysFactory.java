package com.gv.steel.system.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gv.steel.common.mybatis.base.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 工厂管理
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_factory")
@Schema(name = "SysFactory对象", description = "工厂管理")
public class SysFactory extends BaseEntity<SysFactory> {

    private static final long serialVersionUID = 1L;

    @Schema(description = "工厂简称")
    private String factoryNickname;

    @Schema(description = "工厂名称")
    private String factoryName;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "联系电话")
    private String tel;

    @Schema(description = "传真")
    private String fax;

    @Schema(description = "工厂编码")
    private String factoryCode;

    @Schema(description = "排序")
    private Integer sortOrder;


    @Override
    public Serializable pkVal() {
        return this.getId();
    }

}
