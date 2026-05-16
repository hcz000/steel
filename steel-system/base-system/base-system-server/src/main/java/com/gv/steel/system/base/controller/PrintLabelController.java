package com.gv.steel.system.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.mybatis.utils.QueryWrapperUtil;
import com.gv.steel.common.security.annotation.Inner;
import com.gv.steel.system.base.dto.PrintLabelQueryDTO;
import com.gv.steel.system.base.entity.PrintLabel;
import com.gv.steel.system.base.service.PrintLabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 打印标签 前端控制器
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Tag(name = "打印标签接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/print-label")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class PrintLabelController {

    private final PrintLabelService printLabelService;

    /**
     * 打印标签列表
     */
    @Operation(summary = "打印标签查询列表")
    @Parameters({
            @Parameter(name = "current", description = "当前页"),
            @Parameter(name = "size", description = "每页显示条数"),
            @Parameter(name = "labelName", description = "标签名称"),
            @Parameter(name = "createTime", description = "创建时间, format: yyyy-MM-dd,yyyy-MM-dd")
    })
    @GetMapping("/page")
    public Result<PageResult<PrintLabel>> getPage(@Parameter(hidden = true) Page<PrintLabel> page,
                                                  @Parameter(hidden = true) PrintLabelQueryDTO dto) {
        QueryWrapper<PrintLabel> wrapper = QueryWrapperUtil.queryWrapperHandler(dto);
        return Result.ok(PageResult.<PrintLabel>builder().build().pageResult(printLabelService.page(page, wrapper)));
    }

    /**
     * 打印标签列表查询
     */
    @Operation(summary = "打印标签列表查询")
    @Parameters({
            @Parameter(name = "labelType", description = "标签类型（0-标签模板，1-其他模板）")
    })
    @GetMapping("/list")
    public Result<List<PrintLabel>> findDeliveryCategoryList(PrintLabelQueryDTO dto) {
        QueryWrapper<PrintLabel> wrapper = QueryWrapperUtil.queryWrapperHandler(dto);
        return Result.ok(printLabelService.list(wrapper));
    }

    /**
     * 打印标签查询
     */
    @Operation(summary = "打印标签查询")
    @Parameter(name = "id", description = "打印标签ID", required = true)
    @GetMapping("/{id:\\d+}")
    public Result<PrintLabel> findPrintLabelById(@PathVariable Long id) {
        return Result.ok(printLabelService.getById(id));
    }

    /**
     * 打印标签新增
     */
    @Operation(summary = "打印标签新增")
    @PostMapping
    public Result<Boolean> save(@RequestBody @Valid PrintLabel printLabel) {
        return Result.ok(printLabelService.save(printLabel));
    }

    /**
     * 打印标签修改
     */
    @Operation(summary = "打印标签修改")
    @PutMapping
    public Result<Boolean> update(@RequestBody @Valid PrintLabel printLabel) {
        return Result.ok(printLabelService.updateById(printLabel));
    }

    /**
     * 打印标签删除
     */
    @Operation(summary = "打印标签删除")
    @Parameter(name = "id", description = "打印标签ID", required = true)
    @DeleteMapping("/{id:\\d+}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.ok(printLabelService.removeById(id));
    }

    @Inner(false)
    @Operation(summary = "通过标签Id获取标签模板文件")
    @GetMapping("template/key/{id:\\d+}")
    public void getTemplateByCode(@PathVariable Long id, HttpServletResponse response) {
        printLabelService.getTemplateById(id, response);
    }

    @Inner(false)
    @Operation(summary = "通过标签Code获取标签模板文件")
    @GetMapping("template/code/{code}")
    public void getTemplateByCode(@PathVariable String code, HttpServletResponse response) {
        printLabelService.getTemplateByCode(code, response);
    }
}
