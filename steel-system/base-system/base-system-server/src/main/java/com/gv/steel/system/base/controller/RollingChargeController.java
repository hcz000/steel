package com.gv.steel.system.base.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.security.annotation.Inner;
import com.gv.steel.system.base.dto.RollingChargeQueryDTO;
import com.gv.steel.system.base.entity.RollingCharge;
import com.gv.steel.system.base.service.RollingChargeService;
import com.gv.steel.system.base.vo.RollingChargePageVO;
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
 * 压延收费 前端控制器
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Tag(name = "压延收费接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rolling-charge")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class RollingChargeController {

    private final RollingChargeService rollingChargeService;

    @Inner(false)
    @Operation(summary = "通过委托单位获取加工收费列表")
    @GetMapping("list")
    public Result<List<RollingCharge>> list(@Parameter(description = "委托单位ID") @RequestParam Long customerId) {
        return Result.ok(rollingChargeService.findListByCustomerId(customerId));
    }

    /**
     * 压延收费列表
     */
    @Operation(summary = "压延收费查询列表")
    @Parameters({
            @Parameter(name = "current", description = "当前页"),
            @Parameter(name = "size", description = "每页显示条数"),
            @Parameter(name = "factoryId", description = "加工厂ID"),
            @Parameter(name = "customerId", description = "客户ID"),
            @Parameter(name = "contractNo", description = "合同号"),
            @Parameter(name = "materialQualityId", description = "材质ID"),
            @Parameter(name = "model", description = "型号"),
            @Parameter(name = "rawMaterialPly", description = "原卷厚度"),
            @Parameter(name = "productPly", description = "成品厚度")
    })
    @GetMapping("/page")
    public Result<PageResult<RollingChargePageVO>> getPage(@Parameter(hidden = true) Page<RollingChargePageVO> page,
                                                           @Parameter(hidden = true) RollingChargeQueryDTO dto) {
        return Result.ok(rollingChargeService.pageRollingCharge(page, dto));
    }

    /**
     * 压延收费查询
     */
    @Operation(summary = "压延收费查询", hidden = true)
    @Parameter(name = "id", description = "压延收费ID", required = true)
    @GetMapping("/{id:\\d+}")
    public Result<RollingCharge> findRollingChargeById(@PathVariable Long id) {
        return Result.ok(rollingChargeService.getById(id));
    }

    /**
     * 压延收费新增
     */
    @Operation(summary = "压延收费新增", hidden = true)
    @PostMapping
    public Result<Boolean> save(@RequestBody @Valid RollingCharge rollingCharge) {
        return Result.ok(rollingChargeService.save(rollingCharge));
    }

    /**
     * 压延收费修改
     */
    @Operation(summary = "压延收费修改", hidden = true)
    @PutMapping
    public Result<Boolean> update(@RequestBody @Valid RollingCharge rollingCharge) {
        return Result.ok(rollingChargeService.updateById(rollingCharge));
    }

    /**
     * 压延收费删除
     */
    @Operation(summary = "压延收费删除", hidden = true)
    @Parameter(name = "id", description = "压延收费ID", required = true)
    @DeleteMapping("/{id:\\d+}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.ok(rollingChargeService.removeById(id));
    }
}
