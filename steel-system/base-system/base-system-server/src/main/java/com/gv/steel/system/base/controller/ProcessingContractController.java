package com.gv.steel.system.base.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.security.annotation.Inner;
import com.gv.steel.system.base.dto.ProcessingContractDTO;
import com.gv.steel.system.base.dto.ProcessingContractQueryDTO;
import com.gv.steel.system.base.service.ProcessingContractService;
import com.gv.steel.system.base.vo.ProcessingContractDetailVO;
import com.gv.steel.system.base.vo.ProcessingContractPageVO;
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
 * 加工合同 前端控制器
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Tag(name = "加工合同接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/processing-contract")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class ProcessingContractController {

    private final ProcessingContractService processingContractService;

    /**
     * 加工合同列表
     */
    @Operation(summary = "加工合同查询列表")
    @Parameters({
            @Parameter(name = "current", description = "当前页"),
            @Parameter(name = "size", description = "每页显示条数"),
            @Parameter(name = "customerId", description = "委托单位ID"),
            @Parameter(name = "contractNo", description = "合同号"),
            @Parameter(name = "signedDate", description = "签订日期范围，“yyyy-MM-dd,yyyy-MM-dd”")
    })
    @GetMapping("/page")
    public Result<PageResult<ProcessingContractPageVO>> getPage(@Parameter(hidden = true) Page<ProcessingContractPageVO> page, @Parameter(hidden = true) ProcessingContractQueryDTO dto) {
        return Result.ok(processingContractService.pageProcessingContract(page, dto));
    }

    /**
     * 加工合同查询
     */
    @Inner(false)
    @Operation(summary = "通过委托单位ID加工合同查询")
    @Parameter(name = "id", description = "委托单位ID", required = true)
    @GetMapping("/customer-contract/{customerId:\\d+}")
    public Result<ProcessingContractDetailVO> findProcessingContractByCustomerId(@PathVariable Long customerId) {
        return Result.ok(processingContractService.getProcessingContractDetailByCustomerId(customerId));
    }

    /**
     * 加工合同查询
     */
    @Operation(summary = "加工合同查询")
    @Parameter(name = "id", description = "加工合同ID", required = true)
    @GetMapping("/{id:\\d+}")
    public Result<ProcessingContractDetailVO> findProcessingContractById(@PathVariable Long id) {
        return Result.ok(processingContractService.getProcessingContractDetailById(id));
    }

    /**
     * 加工合同新增
     */
    @Operation(summary = "加工合同新增")
    @PostMapping
    public Result<Long> save(@RequestBody @Valid ProcessingContractDTO dto) {
        return Result.ok(processingContractService.saveProcessContract(dto));
    }

    /**
     * 加工合同修改
     */
    @Operation(summary = "加工合同修改")
    @PutMapping
    public Result<Long> update(@RequestBody @Valid ProcessingContractDTO dto) {
        return Result.ok(processingContractService.updateProcessContract(dto));
    }

    /**
     * 加工合同删除
     */
    @Operation(summary = "加工合同删除")
    @Parameter(name = "id", description = "加工合同ID", required = true)
    @DeleteMapping("/{id:\\d+}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.ok(processingContractService.removeProcessContractById(id));
    }
}
