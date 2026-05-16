package com.gv.steel.system.base.controller;

import com.gv.steel.common.core.api.Result;
import com.gv.steel.system.base.entity.Folder;
import com.gv.steel.system.base.service.FolderService;
import com.gv.steel.system.base.vo.FolderFileVO;
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
 * 共享文件夹表 前端控制器
 * </p>
 *
 * @author administrator
 * @since 2023-11-13
 */
@Tag(name = "共享文件夹表接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/folder")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class FolderController {

    private final FolderService folderService;

    /**
     * 共享文件夹表查询列表
     */
    @Operation(summary = "共享文件夹表查询列表")
    @GetMapping("/list/{folderId:\\d+}")
    public Result<List<FolderFileVO>> getList(@PathVariable @Parameter(description = "目录ID") Long folderId) {
        return Result.ok(folderService.listFolderFile(folderId));
    }

    @Operation(summary = "查询共享文件")
    @GetMapping("/search")
    public Result<List<FolderFileVO>> getListByName(@Parameter(description = "文件名称") @RequestParam String fileName) {
        return Result.ok(folderService.getListByName(fileName));
    }

    /**
     * 共享文件夹表查询
     */
    @Operation(summary = "共享文件夹表查询")
    @Parameter(name = "id", description = "共享文件夹表ID", required = true)
    @GetMapping("/{id:\\d+}")
    public Result<Folder> findFolderById(@PathVariable Long id) {
        return Result.ok(folderService.getById(id));
    }

    /**
     * 共享文件夹表新增
     */
    @Operation(summary = "共享文件夹表新增")
    @PostMapping
    public Result<Boolean> save(@RequestBody @Valid Folder folder) {
        return Result.ok(folderService.saveFolder(folder));
    }

    /**
     * 共享文件夹表修改
     */
    @Operation(summary = "共享文件夹表修改")
    @PutMapping
    public Result<Boolean> update(@RequestBody @Valid Folder folder) {
        return Result.ok(folderService.updateFolderById(folder));
    }

    /**
     * 共享文件夹表删除
     */
    @Operation(summary = "共享文件夹表删除(递归删除子目录和文件)")
    @Parameter(name = "id", description = "共享文件夹表ID", required = true)
    @DeleteMapping("/{id:\\d+}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.ok(folderService.removeFolderById(id));
    }
}
