package com.gv.steel.system.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.mybatis.utils.QueryWrapperUtil;
import com.gv.steel.system.base.dto.InvoiceTitleQueryDTO;
import com.gv.steel.system.base.entity.InvoiceTitle;
import com.gv.steel.system.base.service.InvoiceTitleService;
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
 * 发票抬头 前端控制器
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Tag(name = "发票抬头接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/invoice-title")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class InvoiceTitleController {

    private final InvoiceTitleService invoiceTitleService;

    /**
     * 发票抬头列表
     */
    @Operation(summary = "发票抬头查询列表")
    @Parameters({
            @Parameter(name = "current", description = "当前页"),
            @Parameter(name = "size", description = "每页显示条数"),
            @Parameter(name = "invoiceTitleName", description = "发票抬头名称"),
            @Parameter(name = "createTime", description = "创建时间, format: yyyy-MM-dd,yyyy-MM-dd")
    })
    @GetMapping("/page")
    public Result<PageResult<InvoiceTitle>> getPage(@Parameter(hidden = true) Page<InvoiceTitle> page,
                                                    @Parameter(hidden = true) InvoiceTitleQueryDTO dto) {
        QueryWrapper<InvoiceTitle> wrapper = QueryWrapperUtil.queryWrapperHandler(dto);
        return Result.ok(PageResult.<InvoiceTitle>builder().build().pageResult(invoiceTitleService.page(page, wrapper)));
    }

    /**
     * 发票抬头列表查询
     */
    @Operation(summary = "发票抬头列表查询")
    @GetMapping("/list")
    public Result<List<InvoiceTitle>> findDeliveryCategoryList() {
        return Result.ok(invoiceTitleService.list(
                        Wrappers.<InvoiceTitle>lambdaQuery()
                                .orderByDesc(InvoiceTitle::getCreateTime)
                )
        );
    }

    /**
     * 发票抬头查询
     */
    @Operation(summary = "发票抬头查询")
    @Parameter(name = "id", description = "发票抬头ID", required = true)
    @GetMapping("/{id:\\d+}")
    public Result<InvoiceTitle> findInvoiceTitleById(@PathVariable Long id) {
        return Result.ok(invoiceTitleService.getById(id));
    }

    /**
     * 发票抬头新增
     */
    @Operation(summary = "发票抬头新增")
    @PostMapping
    public Result<Boolean> save(@RequestBody @Valid InvoiceTitle invoiceTitle) {
        return Result.ok(invoiceTitleService.save(invoiceTitle));
    }

    /**
     * 发票抬头修改
     */
    @Operation(summary = "发票抬头修改")
    @PutMapping
    public Result<Boolean> update(@RequestBody @Valid InvoiceTitle invoiceTitle) {
        return Result.ok(invoiceTitleService.updateById(invoiceTitle));
    }

    /**
     * 发票抬头删除
     */
    @Operation(summary = "发票抬头删除")
    @Parameter(name = "id", description = "发票抬头ID", required = true)
    @DeleteMapping("/{id:\\d+}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.ok(invoiceTitleService.removeById(id));
    }
}
