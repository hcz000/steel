package com.gv.steel.codegen.dto;

import com.baomidou.mybatisplus.annotation.DbType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(name = "CodeGenReqDto", description = "代码生成")
public class CodeGenReqDto implements Serializable {
    private static final long serialVersionUID = 40729323121348021L;

    @Schema(description = "数据源ID")
    private Long datasourceId;

    @Schema(description = "生成代码所在包名")
    private String packageName;

    @Schema(description = "表前缀")
    private String prefix;

    @Schema(description = "实体名")
    private String modelName;

    @Schema(description = "表名，多个使用逗号分隔，为空生成所有表")
    private String tableName;

    @Schema(description = "数据库类型", hidden = true)
    private DbType dbType;

    @Schema(description = "数据库链接地址", hidden = true)
    private String url;

    @Schema(description = "数据库用户名", hidden = true)
    private String username;

    @Schema(description = "数据库密码", hidden = true)
    private String password;

    @Schema(description = "驱动名", hidden = true)
    private String driverName;

    @Schema(description = "文件输出地址", hidden = true)
    private String outputDir;
}
