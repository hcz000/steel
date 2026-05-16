package com.gv.steel.system.base.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.security.annotation.Inner;
import com.gv.steel.system.base.dto.RipCutChargeQueryDTO;
import com.gv.steel.system.base.entity.RipCutCharge;
import com.gv.steel.system.base.service.RipCutChargeService;
import com.gv.steel.system.base.vo.RipCutChargePageVO;
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
 * 纵切收费 前端控制器
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Tag(name = "纵切收费接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rip-cut-charge")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class RipCutChargeController {

    private final RipCutChargeService ripCutChargeService;

    @Inner(false)
    @Operation(summary = "通过委托单位获取加工收费列表")
    @GetMapping("list")
    public Result<List<RipCutCharge>> list(@Parameter(description = "委托单位ID") @RequestParam Long customerId) {
        return Result.ok(ripCutChargeService.findListByCustomerId(customerId));
    }

    /**
     * 纵切收费列表
     */
    @Operation(summary = "纵切收费查询列表")
    @Parameters({
            @Parameter(name = "current", description = "当前页"),
            @Parameter(name = "size", description = "每页显示条数"),
            @Parameter(name = "factoryId", description = "加工厂ID"),
            @Parameter(name = "customerId", description = "客户ID"),
            @Parameter(name = "contractNo", description = "合同号"),
            @Parameter(name = "materialQualityId", description = "材质ID"),
            @Parameter(name = "rawMaterialPly", description = "原卷厚度"),
            @Parameter(name = "rawMaterialWidth", description = "原卷宽度"),
    })
    @GetMapping("/page")
    public Result<PageResult<RipCutChargePageVO>> getPage(@Parameter(hidden = true) Page<RipCutChargePageVO> page,
                                                          @Parameter(hidden = true) RipCutChargeQueryDTO dto) {
        return Result.ok(ripCutChargeService.pageRipCutCharge(page, dto));
    }

    /**
     * 纵切收费查询
     */
    @Operation(summary = "纵切收费查询", hidden = true)
    @Parameter(name = "id", description = "纵切收费ID", required = true)
    @GetMapping("/{id:\\d+}")
    public Result<RipCutCharge> findRipCutChargeById(@PathVariable Long id) {
        return Result.ok(ripCutChargeService.getById(id));
    }

    /**
     * 纵切收费新增
     */
    @Operation(summary = "纵切收费新增", hidden = true)
    @PostMapping
    public Result<Boolean> save(@RequestBody @Valid RipCutCharge ripCutCharge) {
        return Result.ok(ripCutChargeService.save(ripCutCharge));
    }

    /**
     * 纵切收费修改
     */
    @Operation(summary = "纵切收费修改", hidden = true)
    @PutMapping
    public Result<Boolean> update(@RequestBody @Valid RipCutCharge ripCutCharge) {
        return Result.ok(ripCutChargeService.updateById(ripCutCharge));
    }

    /**
     * 纵切收费删除
     */
    @Operation(summary = "纵切收费删除", hidden = true)
    @Parameter(name = "id", description = "纵切收费ID", required = true)
    @DeleteMapping("/{id:\\d+}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.ok(ripCutChargeService.removeById(id));
    }
}
