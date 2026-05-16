package com.gv.steel.system.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 用户班组关联关系表
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user_team")
@Schema(name = "SysUserTeam对象", description = "用户班组关联关系表")
public class SysUserTeam extends Model<SysUserTeam> {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "工厂ID")
    private Long factoryId;

    @Schema(description = "班组ID")
    private Long teamId;


    @Override
    public Serializable pkVal() {
        return this.userId;
    }

}
