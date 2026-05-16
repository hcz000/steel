package com.gv.steel.system.base.controller;

import com.gv.steel.common.core.api.Result;
import com.gv.steel.system.base.entity.ShareFile;
import com.gv.steel.system.base.service.ShareFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 共享文件表 前端控制器
 * </p>
 *
 * @author administrator
 * @since 2023-11-13
 */
@Tag(name = "共享文件表接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/share-file")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class ShareFileController {

    private final ShareFileService shareFileService;

    /**
     * 共享文件表查询
     */
    @Operation(summary = "共享文件表查询")
    @Parameter(name = "id", description = "共享文件表ID", required = true)
    @GetMapping("/{id:\\d+}")
    public Result<ShareFile> findShareFileById(@PathVariable Long id) {
        return Result.ok(shareFileService.getById(id));
    }

    /**
     * 共享文件表新增
     */
    @Operation(summary = "共享文件表新增")
    @PostMapping
    public Result<Boolean> save(@RequestBody @Valid ShareFile shareFile) {
        return Result.ok(shareFileService.saveShareFile(shareFile));
    }

    /**
     * 共享文件表新增
     */
    @Operation(summary = "共享文件表批量新增")
    @PostMapping("/batch")
    public Result<Boolean> save(@RequestBody @Valid List<ShareFile> shareFiles) {
        return Result.ok(shareFileService.batchSaveShareFile(shareFiles));
    }

    /**
     * 共享文件表修改
     */
    @Operation(summary = "共享文件表修改")
    @PutMapping
    public Result<Boolean> update(@RequestBody @Valid ShareFile shareFile) {
        return Result.ok(shareFileService.updateById(shareFile));
    }

    /**
     * 共享文件表删除
     */
    @Operation(summary = "共享文件表删除")
    @Parameter(name = "id", description = "共享文件表ID", required = true)
    @DeleteMapping("/{id:\\d+}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.ok(shareFileService.removeById(id));
    }
}
