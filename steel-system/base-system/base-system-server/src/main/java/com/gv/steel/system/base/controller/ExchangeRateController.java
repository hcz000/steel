package com.gv.steel.system.base.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.system.base.dto.ExchangeRateQueryDTO;
import com.gv.steel.system.base.entity.ExchangeRate;
import com.gv.steel.system.base.service.ExchangeRateService;
import com.gv.steel.system.base.vo.ExchangeRatePageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 汇率管理表 前端控制器
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Tag(name = "汇率管理表接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/exchange-rate")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    /**
     * 汇率管理表列表
     */
    @Operation(summary = "汇率管理表查询列表")
    @Parameters({
            @Parameter(name = "current", description = "当前页"),
            @Parameter(name = "size", description = "每页显示条数"),
            @Parameter(name = "targetShortName", description = "目标币种"),
            @Parameter(name = "sourceShortName", description = "源币种"),
            @Parameter(name = "timeZone", description = "起止时间")
    })
    @GetMapping("/page")
    public Result<PageResult<ExchangeRatePageVO>> getPage(@Parameter(hidden = true) Page<ExchangeRatePageVO> page, @Parameter(hidden = true) ExchangeRateQueryDTO dto) {
        return Result.ok(exchangeRateService.getPage(page, dto));
    }

    /**
     * 汇率管理表查询
     */
    @Operation(summary = "汇率管理表查询", hidden = true)
    @Parameter(name = "id", description = "汇率管理表ID", required = true)
    @GetMapping("/{id:\\d+}")
    public Result<ExchangeRate> findExchangeRateById(@PathVariable Long id) {
        return Result.ok(exchangeRateService.getById(id));
    }

    /**
     * 汇率管理表新增
     */
    @Operation(summary = "汇率管理表新增")
    @PostMapping
    public Result<Boolean> save(@RequestBody @Valid ExchangeRate exchangeRate) {
        return Result.ok(exchangeRateService.saveExchangeRate(exchangeRate));
    }

    /**
     * 汇率管理表修改
     */
    @Operation(summary = "汇率管理表修改")
    @PutMapping
    public Result<Boolean> update(@RequestBody @Valid ExchangeRate exchangeRate) {
        return Result.ok(exchangeRateService.updateExchangeRateById(exchangeRate));
    }

    /**
     * 汇率管理表删除
     */
    @Operation(summary = "汇率管理表删除")
    @Parameter(name = "id", description = "汇率管理表ID", required = true)
    @DeleteMapping("/{id:\\d+}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.ok(exchangeRateService.removeById(id));
    }
}
