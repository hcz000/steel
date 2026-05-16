package com.gv.steel.system.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gv.steel.common.mybatis.base.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 班组管理
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_team")
@Schema(name = "SysTeam对象", description = "班组管理")
public class SysTeam extends BaseEntity<SysTeam> {

    private static final long serialVersionUID = 1L;

    @Schema(description = "班组名称")
    private String teamName;

    @NotNull(message = "factory.id.is.null")
    @Schema(description = "所属工厂ID")
    private Long factoryId;

    @Schema(description = "排序值")
    private Integer sortOrder;


    @Override
    public Serializable pkVal() {
        return this.getId();
    }

}
