package com.gv.steel.system.base.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.system.base.dto.CurrencyPageDTO;
import com.gv.steel.system.base.entity.Currency;
import com.gv.steel.system.base.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 币种管理表 前端控制器
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Tag(name = "币种管理表接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/currency")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class CurrencyController {

    private final CurrencyService currencyService;

    /**
     * 币种管理表列表
     */
    @Operation(summary = "币种管理表查询列表")
    @Parameters({
            @Parameter(name = "current", description = "当前页"),
            @Parameter(name = "size", description = "每页显示条数"),
            @Parameter(name = "name", description = "币种名称"),
            @Parameter(name = "timeZone", description = "时间区间", example = "2000-01-01 01:01:01,2000-01-02 01:01:01")
    })
    @GetMapping("/page")
    public Result<PageResult<Currency>> getPage(@Parameter(hidden = true) Page<Currency> page, @Parameter(hidden = true) CurrencyPageDTO dto) {
        return Result.ok(PageResult.<Currency>builder().build().pageResult(currencyService.getPage(page, dto)));
    }

    /**
     * 币种管理表查询数组
     */
    @Operation(summary = "币种管理表查询数组")
    @GetMapping("/list")
    public Result<List<Currency>> list() {
        return Result.ok(currencyService.list());
    }

    /**
     * 币种管理表查询
     */
    @Operation(summary = "币种管理表查询", hidden = true)
    @Parameter(name = "id", description = "币种管理表ID", required = true)
    @GetMapping("/{id:\\d+}")
    public Result<Currency> findCurrencyById(@PathVariable Long id) {
        return Result.ok(currencyService.getById(id));
    }

    /**
     * 币种管理表新增
     */
    @Operation(summary = "币种管理表新增")
    @PostMapping
    public Result<Boolean> save(@RequestBody @Valid Currency currency) {
        return Result.ok(currencyService.save(currency));
    }

    /**
     * 币种管理表修改
     */
    @Operation(summary = "币种管理表修改")
    @PutMapping
    public Result<Boolean> update(@RequestBody @Valid Currency currency) {
        return Result.ok(currencyService.updateById(currency));
    }

    /**
     * 币种管理表删除
     */
    @Operation(summary = "币种管理表删除")
    @Parameter(name = "id", description = "币种管理表ID", required = true)
    @DeleteMapping("/{id:\\d+}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.ok(currencyService.removeById(id));
    }
}
