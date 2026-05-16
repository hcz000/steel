package com.gv.steel.system.base.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.system.base.entity.HisExchangeRate;
import com.gv.steel.system.base.service.HisExchangeRateService;
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
 * 历史汇率表 前端控制器
 * </p>
 *
 * @author administrator
 * @since 2024-02-05
 */
@Tag(name = "历史汇率表接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/his-exchange-rate")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class HisExchangeRateController {

    private final HisExchangeRateService hisExchangeRateService;

    /**
     * 历史汇率表列表
     */
    @Operation(summary = "历史汇率表查询列表", hidden = true)
    @Parameters({
            @Parameter(name = "current", description = "当前页"),
            @Parameter(name = "size", description = "每页显示条数")
    })
    @GetMapping("/page")
    public Result<PageResult<HisExchangeRate>> getPage(@Parameter(hidden = true) Page<HisExchangeRate> page) {
        return Result.ok(PageResult.<HisExchangeRate>builder().build().pageResult(hisExchangeRateService.page(page)));
    }

    @Operation(summary = "通过汇率ID查询历史汇率")
    @Parameter(name = "nid", description = "汇率ID")
    @GetMapping("/list/{nid:\\d+}")
    public Result<List<HisExchangeRate>> getList(@Parameter(hidden = true) @PathVariable Long nid) {
        return Result.ok(
                hisExchangeRateService.list(
                        hisExchangeRateService.lambdaQuery()
                                .eq(HisExchangeRate::getNid, nid)
                                .orderByDesc(HisExchangeRate::getCreateTime)
                )
        );
    }

    /**
     * 历史汇率表查询
     */
    @Operation(summary = "历史汇率表查询", hidden = true)
    @Parameter(name = "id", description = "历史汇率表ID", required = true)
    @GetMapping("/{id:\\d+}")
    public Result<HisExchangeRate> findHisExchangeRateById(@PathVariable Long id) {
        return Result.ok(hisExchangeRateService.getById(id));
    }

    /**
     * 历史汇率表新增
     */
    @Operation(summary = "历史汇率表新增", hidden = true)
    @PostMapping
    public Result<Boolean> save(@RequestBody @Valid HisExchangeRate hisExchangeRate) {
        return Result.ok(hisExchangeRateService.save(hisExchangeRate));
    }

    /**
     * 历史汇率表修改
     */
    @Operation(summary = "历史汇率表修改", hidden = true)
    @PutMapping
    public Result<Boolean> update(@RequestBody @Valid HisExchangeRate hisExchangeRate) {
        return Result.ok(hisExchangeRateService.updateById(hisExchangeRate));
    }

    /**
     * 历史汇率表删除
     */
    @Operation(summary = "历史汇率表删除", hidden = true)
    @Parameter(name = "id", description = "历史汇率表ID", required = true)
    @DeleteMapping("/{id:\\d+}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.ok(hisExchangeRateService.removeById(id));
    }
}
