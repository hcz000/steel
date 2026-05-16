package com.gv.steel.system.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.mybatis.utils.QueryWrapperUtil;
import com.gv.steel.system.base.dto.DeliveryCategoryQueryDTO;
import com.gv.steel.system.base.entity.DeliveryCategory;
import com.gv.steel.system.base.service.DeliveryCategoryService;
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
 * 出货单类型 前端控制器
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Tag(name = "出货单类型接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/delivery-category")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class DeliveryCategoryController {

    private final DeliveryCategoryService deliveryCategoryService;

    /**
     * 出货单类型列表
     */
    @Operation(summary = "出货单类型查询列表")
    @Parameters({
            @Parameter(name = "current", description = "当前页"),
            @Parameter(name = "size", description = "每页显示条数"),
            @Parameter(name = "deliveryOrderName", description = "出货单类型名称"),
            @Parameter(name = "createTime", description = "创建时间，format: yyyy-MM-dd,yyyy-MM-dd")
    })
    @GetMapping("/page")
    public Result<PageResult<DeliveryCategory>> getPage(@Parameter(hidden = true) Page<DeliveryCategory> page, @Parameter(hidden = true) DeliveryCategoryQueryDTO dto) {
        QueryWrapper<DeliveryCategory> wrapper = QueryWrapperUtil.queryWrapperHandler(dto);
        return Result.ok(PageResult.<DeliveryCategory>builder().build().pageResult(deliveryCategoryService.page(page, wrapper)));
    }

    /**
     * 出货单类型列表查询
     */
    @Operation(summary = "出货单类型列表")
    @GetMapping("/list")
    public Result<List<DeliveryCategory>> findDeliveryCategoryList() {
        return Result.ok(deliveryCategoryService.list(
                        Wrappers.<DeliveryCategory>lambdaQuery()
                                .orderByDesc(DeliveryCategory::getCreateTime)
                                .select(DeliveryCategory::getId, DeliveryCategory::getDeliveryOrderName, DeliveryCategory::getDeliveryOrderCode)
                )
        );
    }

    /**
     * 出货单类型查询
     */
    @Operation(summary = "出货单类型查询")
    @Parameter(name = "id", description = "出货单类型ID", required = true)
    @GetMapping("/{id:\\d+}")
    public Result<DeliveryCategory> findDeliveryCategoryById(@PathVariable Long id) {
        return Result.ok(deliveryCategoryService.getById(id));
    }

    /**
     * 出货单类型新增
     */
    @Operation(summary = "出货单类型新增")
    @PostMapping
    public Result<Boolean> save(@RequestBody @Valid DeliveryCategory deliveryCategory) {
        return Result.ok(deliveryCategoryService.save(deliveryCategory));
    }

    /**
     * 出货单类型修改
     */
    @Operation(summary = "出货单类型修改")
    @PutMapping
    public Result<Boolean> update(@RequestBody @Valid DeliveryCategory deliveryCategory) {
        return Result.ok(deliveryCategoryService.updateById(deliveryCategory));
    }

    /**
     * 出货单类型删除
     */
    @Operation(summary = "出货单类型删除")
    @Parameter(name = "id", description = "出货单类型ID", required = true)
    @DeleteMapping("/{id:\\d+}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.ok(deliveryCategoryService.removeById(id));
    }
}
