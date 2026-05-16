package com.gv.steel.system.base.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.system.base.dto.AccessoryChargeQueryDTO;
import com.gv.steel.system.base.entity.AccessoryCharge;
import com.gv.steel.system.base.service.AccessoryChargeService;
import com.gv.steel.system.base.vo.AccessoryChargePageVO;
import com.gv.steel.system.base.vo.ProcessAccessoryVO;
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
 * 辅料收费 前端控制器
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Tag(name = "辅料收费接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/accessory-charge")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class AccessoryChargeController {

    private final AccessoryChargeService accessoryChargeService;

    @Operation(summary = "通过委托单位ID和辅料的使用范围获取辅料列表")
    @Parameters({
            @Parameter(name = "customerId", description = "委托单位ID", required = true),
            @Parameter(name = "useScope", description = "使用范围(0：整单适用，1：单件适用，2：打包适用)", required = true)
    })
    @GetMapping("/processAccessoryList")
    public Result<List<ProcessAccessoryVO>> getAccessoryChargeByScope(@RequestParam("customerId") Long customerId,
                                                                      @RequestParam("useScope") Integer useScope) {
        return Result.ok(accessoryChargeService.getAccessoryChargeByScope(customerId, useScope));
    }

    @Operation(summary = "通过委托单位ID获取辅料列表")
    @Parameters({
            @Parameter(name = "customerId", description = "委托单位ID", required = true)
    })
    @GetMapping("/list")
    public Result<List<AccessoryCharge>> getAccessoryChargeList(@RequestParam("customerId") Long customerId) {
        return Result.ok(accessoryChargeService.getAccessoryChargeListByCustomer(customerId));
    }

    /**
     * 辅料收费列表
     */
    @Operation(summary = "辅料收费查询列表")
    @Parameters({
            @Parameter(name = "current", description = "当前页"),
            @Parameter(name = "size", description = "每页显示条数"),
            @Parameter(name = "factoryId", description = "加工厂ID"),
            @Parameter(name = "customerId", description = "客户ID"),
            @Parameter(name = "contractNo", description = "合同号"),
            @Parameter(name = "chargeItem", description = "收费项"),
            @Parameter(name = "chargeWay", description = "收费方式（0-按辅料数量，1-按成品重量，2-按原卷重量）"),
            @Parameter(name = "useScope", description = "使用范围;0：整单适用，1：单件适用，2：捆包适用"),
    })
    @GetMapping("/page")
    public Result<PageResult<AccessoryChargePageVO>> getPage(@Parameter(hidden = true) Page<AccessoryChargePageVO> page,
                                                             @Parameter(hidden = true) AccessoryChargeQueryDTO dto) {
        return Result.ok(accessoryChargeService.pageAccessoryCharge(page, dto));
    }

    /**
     * 辅料收费查询
     */
    @Operation(summary = "辅料收费查询", hidden = true)
    @Parameter(name = "id", description = "辅料收费ID", required = true)
    @GetMapping("/{id:\\d+}")
    public Result<AccessoryCharge> findAccessoryChargeById(@PathVariable Long id) {
        return Result.ok(accessoryChargeService.getById(id));
    }

    /**
     * 辅料收费新增
     */
    @Operation(summary = "辅料收费新增", hidden = true)
    @PostMapping
    public Result<Boolean> save(@RequestBody @Valid AccessoryCharge accessoryCharge) {
        return Result.ok(accessoryChargeService.save(accessoryCharge));
    }

    /**
     * 辅料收费修改
     */
    @Operation(summary = "辅料收费修改", hidden = true)
    @PutMapping
    public Result<Boolean> update(@RequestBody @Valid AccessoryCharge accessoryCharge) {
        return Result.ok(accessoryChargeService.updateById(accessoryCharge));
    }

    /**
     * 辅料收费删除
     */
    @Operation(summary = "辅料收费删除", hidden = true)
    @Parameter(name = "id", description = "辅料收费ID", required = true)
    @DeleteMapping("/{id:\\d+}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.ok(accessoryChargeService.removeById(id));
    }
}
