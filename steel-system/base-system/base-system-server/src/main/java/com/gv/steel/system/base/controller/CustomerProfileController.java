package com.gv.steel.system.base.controller;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.easyexcel.annotation.EasyExcelImport;
import com.gv.steel.common.security.annotation.Inner;
import com.gv.steel.system.base.dto.CustomerProfileDTO;
import com.gv.steel.system.base.dto.CustomerProfileQueryDTO;
import com.gv.steel.system.base.dto.CustomerProfileSelectItemQueryDTO;
import com.gv.steel.system.base.dto.excel.CustomerProfileImportDTO;
import com.gv.steel.system.base.dto.excel.CustomerRequireImportDTO;
import com.gv.steel.system.base.dto.processing.AwaitStatementCustomerQueryDTO;
import com.gv.steel.system.base.entity.CustomerProfile;
import com.gv.steel.system.base.service.CustomerProfileService;
import com.gv.steel.system.base.vo.CustomerProfileDetailVO;
import com.gv.steel.system.base.vo.CustomerProfilePageVO;
import com.gv.steel.system.base.vo.CustomerProfileSelectItemVO;
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
 * 客户档案（委托单位、贸易客户） 前端控制器
 * </p>
 *
 * @author administrator
 * @since 2023-11-08
 */
@Tag(name = "客户档案（委托单位、贸易客户）接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/customer-profile")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class CustomerProfileController {

    private final CustomerProfileService customerProfileService;

    /**
     * 委托单位列表
     */
    @Operation(summary = "委托单位分页查询列表")
    @Parameters({
            @Parameter(name = "current", description = "当前页"),
            @Parameter(name = "size", description = "每页显示条数"),
            @Parameter(name = "factoryId", description = "加工厂ID"),
            @Parameter(name = "customerCode", description = "客户代码"),
            @Parameter(name = "customerName", description = "客户名称"),
            @Parameter(name = "createTime", description = "创建时间，format: yyyy-MM-dd,yyyy-MM-dd")
    })
    @GetMapping("/delegate/page")
    public Result<PageResult<CustomerProfilePageVO>> getDelegatePage(@Parameter(hidden = true) Page<CustomerProfilePageVO> page,
                                                                     @Parameter(hidden = true) CustomerProfileQueryDTO dto) {
        return Result.ok(customerProfileService.pageDelegateCustomer(page, dto));
    }

    /**
     * 贸易客户列表
     */
    @Operation(summary = "贸易客户分页查询列表")
    @Parameters({
            @Parameter(name = "current", description = "当前页"),
            @Parameter(name = "size", description = "每页显示条数"),
            @Parameter(name = "customerId", description = "委托单位ID"),
            @Parameter(name = "customerCode", description = "客户代码"),
            @Parameter(name = "customerName", description = "客户名称"),
            @Parameter(name = "createTime", description = "创建时间，format: yyyy-MM-dd,yyyy-MM-dd")
    })
    @GetMapping("/trade/page")
    public Result<PageResult<CustomerProfilePageVO>> getTradePage(@Parameter(hidden = true) Page<CustomerProfilePageVO> page, @Parameter(hidden = true) CustomerProfileQueryDTO dto) {
        return Result.ok(customerProfileService.pageTradeCustomer(page, dto));
    }

    @Operation(summary = "获取委托单位下拉列表（包含单位名称）")
    @Parameters({
            @Parameter(name = "factoryId", description = "工厂ID"),
            @Parameter(name = "customerType", description = "客户类型(0-委托单位，1-贸易客户)"),
            @Parameter(name = "customerId", description = "委托单位ID"),
    })
    @GetMapping("/customer/list")
    public Result<List<CustomerProfileSelectItemVO>> getCustomerList(@Parameter(hidden = true) CustomerProfileSelectItemQueryDTO dto) {
        return Result.ok(customerProfileService.getCustomerSelectList(dto));
    }

    /**
     * 客户档案（委托单位、贸易客户）查询
     */
    @Operation(summary = "客户档案（委托单位、贸易客户）查询")
    @Parameter(name = "id", description = "客户档案（委托单位、贸易客户）ID", required = true)
    @GetMapping("/{id:\\d+}")
    public Result<CustomerProfileDetailVO> findCustomerProfileById(@PathVariable Long id) {
        return Result.ok(customerProfileService.getCustomerDetailById(id));
    }

    @Operation(summary = "根据客户Code值获取客户档案")
    @GetMapping("/getByCode")
    public Result<CustomerProfile> getCustomerProfileByCode(@RequestParam("customerCode") String customerCode, @RequestParam(value = "factoryId", required = false) Long factoryId) {
        return Result.ok(
                customerProfileService.getOne(
                        Wrappers.<CustomerProfile>lambdaQuery()
                                .eq(ObjUtil.isNotNull(factoryId), CustomerProfile::getFactoryId, factoryId)
                                .eq(CustomerProfile::getCustomerCode, customerCode)
                )
        );
    }

    /**
     * 客户档案（委托单位、贸易客户）新增
     */
    @Operation(summary = "客户档案（委托单位、贸易客户）新增")
    @PostMapping
    public Result<Long> save(@RequestBody @Valid CustomerProfileDTO customerProfile) {
        return Result.ok(customerProfileService.saveCustomerProfileDetail(customerProfile));
    }

    /**
     * 客户档案（委托单位、贸易客户）修改
     */
    @Operation(summary = "客户档案（委托单位、贸易客户）修改")
    @PutMapping
    public Result<Long> update(@RequestBody @Valid CustomerProfileDTO customerProfile) {
        return Result.ok(customerProfileService.updateCustomerProfileDetail(customerProfile));
    }

    /**
     * 客户档案（委托单位、贸易客户）删除
     */
    @Operation(summary = "客户档案（委托单位、贸易客户）删除")
    @Parameter(name = "id", description = "客户档案（委托单位、贸易客户）ID", required = true)
    @DeleteMapping("/{id:\\d+}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.ok(customerProfileService.removeCustomerById(id));
    }

    /**
     * 通过月结方式获取委托单位（货主）
     */
    @Operation(summary = "通过月结方式获取委托单位（货主）ID")
    @Parameter(name = "monthlyStatementWay", description = "月结方式", required = true)
    @Inner
    @GetMapping("/getIdListByMonthlyStatementWaW")
    public Result<List<Long>> getIdListByMonthlyStatementWay(@RequestParam Integer monthlyStatementWay) {
        return Result.ok(customerProfileService.getIdListByMonthlyStatementWay(monthlyStatementWay));
    }

    @Operation(summary = "获取待对账的委托单位列表")
    @GetMapping("/pageAwaitStatementDelegateCustomer")
    public Result<PageResult<CustomerProfile>> pageAwaitStatementDelegateCustomer(@Parameter(hidden = true) Page<CustomerProfile> page,
                                                                                  @Parameter(hidden = true) AwaitStatementCustomerQueryDTO dto) {
        return Result.ok(customerProfileService.pageAwaitStatementDelegateCustomer(page, dto));
    }

    @Operation(summary = "客户档案导入接口")
    @PostMapping("/customerProfileImport")
    public Result<Boolean> importCustomerProfile(@Parameter(hidden = true) @EasyExcelImport List<CustomerProfileImportDTO> importData,
                                                 @Parameter(hidden = true) @RequestParam("factoryId") Long factoryId) {
        return Result.ok(customerProfileService.importCustomerProfile(importData, factoryId));
    }

    @Operation(summary = "客戶加工要求導入接口")
    @PostMapping("/customerProcessRequireImport")
    public Result<Boolean> importCustomerProcessRequire(@Parameter(hidden = true) @EasyExcelImport List<CustomerRequireImportDTO> importData,
                                                        @Parameter(hidden = true) @RequestParam("factoryId") Long factoryId) {
        return Result.operationResult(customerProfileService.importCustomerProcessRequire(importData, factoryId));
    }

    @Operation(summary = "客户档案导入数据查重")
    @PostMapping("/customerFileImportDataReview")
    public Result<String> customerFileImportDataReview(@Parameter(hidden = true) @EasyExcelImport List<CustomerProfileImportDTO> importData) {
        return Result.ok(customerProfileService.customerFileImportDataReview(importData));
    }
}
