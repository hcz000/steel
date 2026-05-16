package com.gv.steel.system.base.controller;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.system.base.entity.CrosscutRequire;
import com.gv.steel.system.base.entity.CustomerProfile;
import com.gv.steel.system.base.entity.RipCutRequire;
import com.gv.steel.system.base.entity.RollingRequire;
import com.gv.steel.system.base.service.CrosscutRequireService;
import com.gv.steel.system.base.service.RipCutRequireService;
import com.gv.steel.system.base.service.RollingRequireService;
import com.gv.steel.system.base.vo.BasicRequireVO;
import com.gv.steel.system.user.entity.SysFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 加工基本要求接口 前端控制器
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Tag(name = "加工基本要求接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/basic-require")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class BasicRequireController {

    private final CrosscutRequireService crosscutRequireService;

    private final RipCutRequireService ripCutRequireService;

    private final RollingRequireService rollingRequireService;

    /**
     * 通过工厂ID查询横切基本要求
     */
    @Operation(summary = "通过工厂ID查询横切基本要求")
    @GetMapping("/factoryCrosscutRequire/{factoryId:\\d+}")
    public Result<CrosscutRequire> getCrosscutRequireByFactoryId(@Parameter(description = "工厂ID") @PathVariable Long factoryId) {
        TableName tableNameAnnotation = AnnotationUtil.getAnnotation(SysFactory.class, TableName.class);
        String tableName = tableNameAnnotation.value();
        return Result.ok(crosscutRequireService.getByTableId(factoryId, tableName));
    }

    /**
     * 通过工厂ID查询纵切基本要求
     */
    @Operation(summary = "通过工厂ID查询纵切基本要求")
    @GetMapping("/factoryRipCutRequire/{factoryId:\\d+}")
    public Result<RipCutRequire> getRipCutRequireByFactoryId(@Parameter(description = "工厂ID") @PathVariable Long factoryId) {
        TableName tableNameAnnotation = AnnotationUtil.getAnnotation(SysFactory.class, TableName.class);
        String tableName = tableNameAnnotation.value();
        return Result.ok(ripCutRequireService.getByTableId(factoryId, tableName));
    }

    /**
     * 通过工厂ID查询压延基本要求
     */
    @Operation(summary = "通过工厂ID查询压延基本要求")
    @GetMapping("/factoryRollingRequire/{factoryId:\\d+}")
    public Result<RollingRequire> getRollingRequireByFactoryId(@Parameter(description = "工厂ID") @PathVariable Long factoryId) {
        TableName tableNameAnnotation = AnnotationUtil.getAnnotation(SysFactory.class, TableName.class);
        String tableName = tableNameAnnotation.value();
        return Result.ok(rollingRequireService.getByTableId(factoryId, tableName));
    }


    /**
     * 通过工厂ID查询工厂加工基本要求
     */
    @Operation(summary = "通过工厂ID查询工厂加工基本要求")
    @Parameter(name = "factoryId", description = "工厂ID", required = true)
    @GetMapping("/{factoryId:\\d+}")
    public Result<BasicRequireVO> getRequireByFactoryId(@PathVariable Long factoryId) {
        TableName tableNameAnnotation = AnnotationUtil.getAnnotation(SysFactory.class, TableName.class);
        String tableName = tableNameAnnotation.value();
        return Result.ok(new BasicRequireVO(factoryId, crosscutRequireService.getByTableId(factoryId, tableName),
                ripCutRequireService.getByTableId(factoryId, tableName), rollingRequireService.getByTableId(factoryId, tableName)));
    }

    /**
     * 新增修改工厂基本要求
     */
    @Operation(summary = "新增修改工厂基本要求")
    @PostMapping
    public Result<Boolean> saveOrUpdateBasicRequire(@Parameter @RequestBody BasicRequireVO basicRequireVO) {
        Long factoryId = basicRequireVO.getFactoryId();
        TableName tableNameAnnotation = AnnotationUtil.getAnnotation(SysFactory.class, TableName.class);
        String tableName = tableNameAnnotation.value();
        boolean res = true;
        CrosscutRequire crosscutRequire = basicRequireVO.getCrosscutRequire();
        if (ObjectUtil.isNotEmpty(crosscutRequire)) {
            crosscutRequire.setTableId(factoryId);
            crosscutRequire.setTableName(tableName);
            crosscutRequireService.saveOrUpdate(crosscutRequire);
        }
        RollingRequire rollingRequire = basicRequireVO.getRollingRequire();
        if (ObjectUtil.isNotEmpty(rollingRequire)) {
            rollingRequire.setTableId(factoryId);
            rollingRequire.setTableName(tableName);
            rollingRequireService.saveOrUpdate(rollingRequire);
        }
        RipCutRequire ripCutRequire = basicRequireVO.getRipCutRequire();
        if (ObjectUtil.isNotEmpty(ripCutRequire)) {
            ripCutRequire.setTableId(factoryId);
            ripCutRequire.setTableName(tableName);
            ripCutRequireService.saveOrUpdate(ripCutRequire);
        }
        return Result.ok(res);
    }

    @Operation(summary = "通过客户ID获取客户的纵切要求")
    @GetMapping("customerRipCutRequire/{customerId:\\d+}")
    public Result<RipCutRequire> getRipCutRequireByCustomerId(@Parameter(description = "工厂ID") @PathVariable Long customerId) {
        String tableName = TableInfoHelper.getTableInfo(CustomerProfile.class).getTableName();
        return Result.ok(ripCutRequireService.getOne(Wrappers.<RipCutRequire>lambdaQuery().eq(RipCutRequire::getTableId, customerId).eq(RipCutRequire::getTableName, tableName)));
    }

    @Operation(summary = "通过客户ID获取客户的横切要求")
    @GetMapping("customerCrosscutReq/{customerId:\\d+}")
    public Result<CrosscutRequire> getCrosscutRequireByCustomerId(@Parameter(description = "工厂ID") @PathVariable Long customerId) {
        String tableName = TableInfoHelper.getTableInfo(CustomerProfile.class).getTableName();
        return Result.ok(crosscutRequireService.getOne(Wrappers.<CrosscutRequire>lambdaQuery().eq(CrosscutRequire::getTableId, customerId).eq(CrosscutRequire::getTableName, tableName)));
    }

    @Operation(summary = "通过客户ID获取客户的压延要求")
    @GetMapping("customerRollingRequire/{customerId:\\d+}")
    public Result<RollingRequire> getRollingRequireByCustomerId(@Parameter(description = "工厂ID") @PathVariable Long customerId) {
        String tableName = TableInfoHelper.getTableInfo(CustomerProfile.class).getTableName();
        return Result.ok(rollingRequireService.getOne(Wrappers.<RollingRequire>lambdaQuery().eq(RollingRequire::getTableId, customerId).eq(RollingRequire::getTableName, tableName)));
    }
}