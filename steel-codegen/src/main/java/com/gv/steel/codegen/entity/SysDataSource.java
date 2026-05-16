package com.gv.steel.codegen.entity;

import com.gv.steel.common.mybatis.base.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "SysDataSource", description = "数据源配置类")
public class SysDataSource extends BaseEntity<SysDataSource> {
    private static final long serialVersionUID = -4455453937933859826L;

    @Schema(description = "数据源名称")
    private String name;

    @Schema(description = "数据库类型")
    private String dbType;

    @Schema(description = "数据库驱动类")
    private String driverClass;

    @Schema(description = "数据库链接地址")
    private String url;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "数据库描述")
    private String remark;
}
