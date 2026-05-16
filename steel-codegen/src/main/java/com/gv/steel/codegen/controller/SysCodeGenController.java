package com.gv.steel.codegen.controller;

import com.baomidou.mybatisplus.annotation.DbType;
import com.gv.steel.codegen.dto.CodeGenReqDto;
import com.gv.steel.codegen.entity.SysDataSource;
import com.gv.steel.codegen.service.SysDataSourceService;
import com.gv.steel.codegen.util.GeneratorUtils;
import com.gv.steel.common.security.annotation.Inner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Tag(name = "代码生成控制器")
@RequestMapping("/code-gen")
public class SysCodeGenController {

    private final SysDataSourceService sysDataSourceService;

    @Inner(false)
    @GetMapping("generator")
    @Operation(description = "生成代码", summary = "生成代码")
    @Parameters({
            @Parameter(name = "datasourceId", description = "数据源ID"),
            @Parameter(name = "packageName", description = "生成代码所在包名"),
            @Parameter(name = "prefix", description = "表前缀"),
            @Parameter(name = "tableName", description = "表名，多个使用逗号分隔，为空生成所有表"),
    })
    public void execute(@Parameter(hidden = true) CodeGenReqDto genReqDto,
                        HttpServletResponse response) throws IOException {
        SysDataSource sysDataSource = sysDataSourceService.getById(genReqDto.getDatasourceId());
        genReqDto.setDbType(DbType.getDbType(sysDataSource.getDbType()));
        genReqDto.setUrl(sysDataSource.getUrl());
        genReqDto.setUsername(sysDataSource.getUsername());
        genReqDto.setPassword(sysDataSource.getPassword());
        genReqDto.setDriverName(sysDataSource.getDriverClass());
        GeneratorUtils.execute(genReqDto, response.getOutputStream());

    }


}
