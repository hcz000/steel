package com.gv.steel.system.user.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.log.annotation.SysLog;
import com.gv.steel.common.security.annotation.Inner;
import com.gv.steel.system.user.entity.SysFile;
import com.gv.steel.system.user.service.SysFileService;
import com.gv.steel.system.user.vo.FileVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 文件管理
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys-file")
@Tag(name = "文件管理模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysFileController {

    private final SysFileService sysFileService;

    /**
     * 分页查询
     *
     * @param page    分页对象
     * @param sysFile 文件管理
     * @return
     */
    @Operation(summary = "分页查询", description = "分页查询")
    @Parameters({
            @Parameter(name = "current", description = "当前页"),
            @Parameter(name = "size", description = "每页显示条数"),
            @Parameter(name = "fileName", description = "文件名"),
    })
    @GetMapping("/page")
    public Result<PageResult<SysFile>> getSysFilePage(@Parameter(hidden = true) Page<SysFile> page, @Parameter(hidden = true) SysFile sysFile) {
        return Result.ok(PageResult.<SysFile>builder().build().pageResult(sysFileService.page(page, Wrappers.<SysFile>lambdaQuery()
                .like(StrUtil.isNotBlank(sysFile.getFileName()), SysFile::getFileName, sysFile.getFileName()))));
    }

    /**
     * 通过id删除文件管理
     *
     * @param id id
     * @return R
     */
    @Operation(summary = "通过id删除文件管理", description = "通过id删除文件管理")
    @Parameter(name = "id", description = "文件ID")
    @SysLog("删除文件管理")
    @DeleteMapping("/{id:\\d+}")
    @PreAuthorize("@pms.hasPermission('sys_file_del')")
    public Result<Boolean> removeById(@PathVariable Long id) {
        return Result.ok(sysFileService.deleteFile(id));
    }

    /**
     * 上传文件 文件名采用uuid,避免原始文件名中带"-"符号导致下载的时候解析出现异常
     *
     * @param file 资源
     * @return R(/ admin / bucketName / filename)
     */
    @Operation(summary = "上传文件", description = "上传文件 文件名采用uuid,避免原始文件名中带\"-\"符号导致下载的时候解析出现异常")
    @PostMapping(value = "/upload")
    public Result<FileVO> upload(@RequestPart("file") MultipartFile file) {
        return sysFileService.uploadFile(file);
    }

    /**
     * 上传文件 文件名采用uuid,避免原始文件名中带"-"符号导致下载的时候解析出现异常
     *
     * @param files 资源
     * @return R(/ admin / bucketName / filename)
     */
    @Operation(summary = "批量上传文件", description = "上传文件 文件名采用uuid,避免原始文件名中带\"-\"符号导致下载的时候解析出现异常")
    @PostMapping(value = "/batchUpload")
    public Result<List<FileVO>> upload(@RequestPart("files") List<MultipartFile> files) {
        return sysFileService.uploadFiles(files);
    }

    /**
     * 获取文件
     *
     * @param bucket   桶名称
     * @param fileName 文件空间/名称
     * @param response
     * @return
     */
    @Operation(summary = "获取文件", description = "获取文件")
    @Parameters({
            @Parameter(name = "bucket", description = "桶名称"),
            @Parameter(name = "fileName", description = "文件名称")
    })
    @Inner(false)
    @GetMapping("/{bucket}/{fileName}")
    public void file(@PathVariable String bucket, @PathVariable String fileName, HttpServletResponse response) {
        sysFileService.getFile(bucket, fileName, response);
    }

    @Inner(false)
    @Operation(summary = "下载文件", description = "通过文件ID下载文件")
    @GetMapping("/{id:\\d+}")
    public void download(@PathVariable Long id, HttpServletResponse response) {
        sysFileService.downloadById(id, response);
    }

    /**
     * 获取本地（resources）文件
     *
     * @param fileName 文件名称
     * @param response 本地文件
     */
    @Operation(summary = "获取本地（resources）文件", description = "获取本地（resources）文件")
    @Parameter(name = "fileName", description = "文件名称")
    @SneakyThrows
    @GetMapping("/local/{fileName}")
    public void localFile(@PathVariable String fileName, HttpServletResponse response) {
        ClassPathResource resource = new ClassPathResource("file/" + fileName);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IoUtil.copy(resource.getInputStream(), response.getOutputStream());
    }

    /**
     * 获取文件外网的访问地址
     *
     * @param bucket
     * @param fileName
     * @return
     */
    @Operation(summary = "获取文件外网的访问地址", description = "获取文件外网的访问地址")
    @Parameters({
            @Parameter(name = "bucket", description = "桶名称"),
            @Parameter(name = "fileName", description = "文件名称")
    })
    @Inner(false)
    @GetMapping("/online/{bucket}/{fileName}")
    public Result<String> onlineFile(@PathVariable String bucket, @PathVariable String fileName) {
        return Result.ok(sysFileService.onlineFile(bucket, fileName));
    }

}
