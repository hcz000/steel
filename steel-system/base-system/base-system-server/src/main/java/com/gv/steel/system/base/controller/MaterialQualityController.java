package com.gv.steel.system.base.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.system.base.dto.MaterialQualityDTO;
import com.gv.steel.system.base.dto.MaterialQualityPageDTO;
import com.gv.steel.system.base.entity.MaterialQuality;
import com.gv.steel.system.base.service.MaterialQualityService;
import com.gv.steel.system.base.vo.MaterialQualityVO;
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
 * 材质管理表 前端控制器
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Tag(name = "材质管理表接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/material-quality")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class MaterialQualityController {

    private final MaterialQualityService materialQualityService;

    /**
     * 材质管理表列表
     */
    @Operation(summary = "材质管理表查询列表")
    @Parameters({
            @Parameter(name = "current", description = "当前页"),
            @Parameter(name = "size", description = "每页显示条数"),
            @Parameter(name = "name", description = "名称"),
            @Parameter(name = "type", description = "类别"),
            @Parameter(name = "timeZone", description = "时间区间")
    })
    @GetMapping("/page")
    public Result<PageResult<MaterialQualityVO>> getPage(@Parameter(hidden = true) Page<MaterialQuality> page, @Parameter(hidden = true) MaterialQualityPageDTO dto) {
        return Result.ok(materialQualityService.getPage(page, dto));
    }

    /**
     * 材质管理列表查询
     */
    @Operation(summary = "材质管理列表")
    @GetMapping("/list")
    public Result<List<MaterialQuality>> findMaterialQualityList() {
        return Result.ok(materialQualityService.getMaterialQualityList());
    }

    /**
     * 材质管理表查询
     */
    @Operation(summary = "材质管理表查询")
    @Parameter(name = "id", description = "材质管理表ID", required = true)
    @GetMapping("/{id:\\d+}")
    public Result<MaterialQualityVO> findMaterialQualityById(@PathVariable Long id) {
        return Result.ok(materialQualityService.getMaterialQualityById(id));
    }

    /**
     * 材质管理表新增
     */
    @Operation(summary = "材质管理表新增")
    @PostMapping
    public Result<Boolean> save(@RequestBody @Valid MaterialQualityDTO dto) {
        return Result.ok(materialQualityService.saveMaterialQuality(dto));
    }

    /**
     * 材质管理表修改
     */
    @Operation(summary = "材质管理表修改")
    @PutMapping
    public Result<Boolean> update(@RequestBody @Valid MaterialQualityDTO dto) {
        return Result.ok(materialQualityService.updateMaterialQualityById(dto));
    }

    /**
     * 材质管理表删除
     */
    @Operation(summary = "材质管理表删除")
    @Parameter(name = "id", description = "材质管理表ID", required = true)
    @DeleteMapping("/{id:\\d+}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.ok(materialQualityService.removeMaterialQualityById(id));
    }
}
