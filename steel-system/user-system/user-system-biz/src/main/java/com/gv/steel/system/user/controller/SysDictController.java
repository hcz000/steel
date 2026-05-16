package com.gv.steel.system.user.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.annotation.Language;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.core.constant.CacheConstants;
import com.gv.steel.common.log.annotation.SysLog;
import com.gv.steel.system.user.entity.SysDict;
import com.gv.steel.system.user.entity.SysDictItem;
import com.gv.steel.system.user.service.SysDictItemService;
import com.gv.steel.system.user.service.SysDictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/dict")
@Tag(name = "字典管理模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysDictController {

    private final SysDictItemService sysDictItemService;

    private final SysDictService sysDictService;

    /**
     * 通过ID查询字典信息
     *
     * @param id ID
     * @return 字典信息
     */
    @Operation(summary = "通过ID查询字典信息", description = "通过ID查询字典信息")
    @Parameter(name = "id", description = "字典ID")
    @GetMapping("/{id:\\d+}")
    public Result<SysDict> getById(@PathVariable Long id) {
        return Result.ok(sysDictService.getById(id));
    }

    /**
     * 分页查询字典信息
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @Operation(summary = "分页查询字典信息", description = "分页查询字典信息")
    @Parameters({
            @Parameter(name = "current", description = "当前页码"),
            @Parameter(name = "size", description = "每页展示条数"),
            @Parameter(name = "dictKey", description = "字典key"),
            @Parameter(name = "description", description = "字典描述"),
    })
    @GetMapping("/page")
    public Result<PageResult<SysDict>> getDictPage(@Parameter(hidden = true) Page<SysDict> page, @Parameter(hidden = true) SysDict sysDict) {
        return Result.ok(PageResult.<SysDict>builder().build().pageResult(sysDictService.page(page,
                Wrappers.<SysDict>lambdaQuery()
						.eq(StrUtil.isNotBlank(sysDict.getDictKey()), SysDict::getDictKey, sysDict.getDictKey())
                        .like(StrUtil.isNotBlank(sysDict.getDescription()), SysDict::getDescription, sysDict.getDescription())
                        .orderByDesc(SysDict::getCreateTime))));
    }

	/**
	 * 分页查询字典信息
	 *
	 * @return 分页对象
	 */
	@Operation(summary = "分页查询字典信息", description = "分页查询字典信息")
	@Parameters({
			@Parameter(name = "dictKey", description = "字典key"),
	})
	@GetMapping("/dict/{dictKey}")
	public Result<SysDict> getDictPage(@PathVariable String dictKey) {
		return Result.ok(sysDictService.getOne(
				Wrappers.<SysDict>lambdaQuery()
						.eq(SysDict::getDictKey, dictKey)
						.orderByDesc(SysDict::getCreateTime)));
	}

    /**
     * 通过字典类型查找字典
     *
     * @param key 类型
     * @return 同类型字典
     */
    @Operation(summary = "通过字典类型查找字典", description = "通过字典类型查找字典")
    @Parameter(name = "key", description = "字典Key")
    @GetMapping("/key/{key}")
    public Result<List<SysDictItem>> getDictByKey(@PathVariable String key, @Parameter(hidden = true) @Language String language) {
        return Result.ok(sysDictItemService.listByKey(key, language));
    }

    /**
     * 通过字典类型集合查找字典
     *
     * @param keys 类型集合
     * @return 同类型字典
     */
    @Operation(summary = "通过字典类型集合查找字典", description = "通过字典类型集合查找字典")
    @Parameter(name = "keys", description = "字典Key集合")
    @GetMapping("/keys/{keys}")
    public Result<Map<String, List<SysDictItem>>> getDictByKeys(@PathVariable String[] keys, @Parameter(hidden = true) @Language String language) {
        return Result.ok(sysDictItemService.getByDictKeys(keys, language));
    }

    /**
     * 添加字典
     *
     * @param sysDict 字典信息
     * @return success、false
     */
    @Operation(summary = "添加字典", description = "添加字典")
    @SysLog("添加字典")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sys_dict_add')")
    public Result<Boolean> save(@Valid @RequestBody SysDict sysDict) {
        return Result.ok(sysDictService.save(sysDict));
    }

    /**
     * 删除字典，并且清除字典缓存
     *
     * @param id ID
     * @return R
     */
    @Operation(summary = "删除字典，并且清除字典缓存", description = "删除字典，并且清除字典缓存")
    @SysLog("删除字典")
    @Parameter(name = "id", description = "字典ID")
    @DeleteMapping("/{id:\\d+}")
    @PreAuthorize("@pms.hasPermission('sys_dict_del')")
    public Result<Boolean> removeById(@PathVariable Long id) {
        return Result.ok(sysDictService.removeDict(id));
    }

    /**
     * 修改字典
     *
     * @param sysDict 字典信息
     * @return success/false
     */
    @Operation(summary = "修改字典", description = "修改字典")
    @PutMapping
    @SysLog("修改字典")
    @PreAuthorize("@pms.hasPermission('sys_dict_edit')")
    public Result<Boolean> updateById(@Valid @RequestBody SysDict sysDict) {
        return Result.ok(sysDictService.updateDict(sysDict));
    }

    /**
     * 分页查询
     *
     * @param page        分页对象
     * @param sysDictItem 字典项
     * @return
     */
    @Operation(summary = "分页查询", description = "分页查询")
    @Parameters({
            @Parameter(name = "current", description = "当前页"),
            @Parameter(name = "size", description = "每页显示条数"),
            @Parameter(name = "dictId", description = "所属字典ID"),
            @Parameter(name = "dictKey", description = "所属字典Key"),
            @Parameter(name = "value", description = "字典项值"),
            @Parameter(name = "label", description = "字典项标签"),
            @Parameter(name = "type", description = "字典项类型"),
            @Parameter(name = "description", description = "字典项描述"),
    })
    @GetMapping("/item/page")
    public Result<PageResult<SysDictItem>> getSysDictItemPage(@Parameter(hidden = true) Page<SysDictItem> page, @Parameter(hidden = true) SysDictItem sysDictItem) {
        return Result.ok(PageResult.<SysDictItem>builder().build().pageResult(sysDictItemService.page(page, Wrappers.lambdaQuery(sysDictItem).orderByAsc(SysDictItem::getSortOrder))));
    }

    /**
     * 通过id查询字典项
     *
     * @param id id
     * @return R
     */
    @Operation(summary = "通过id查询字典项", description = "通过id查询字典项")
    @Parameter(name = "id", description = "字典项ID")
    @GetMapping("/item/{id:\\d+}")
    public Result<SysDictItem> getDictItemById(@PathVariable("id") Long id) {
        return Result.ok(sysDictItemService.getById(id));
    }

    /**
     * 新增字典项
     *
     * @param sysDictItem 字典项
     * @return R
     */
    @Operation(summary = "新增字典项", description = "新增字典项")
    @SysLog("新增字典项")
    @PostMapping("/item")
    @CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
    public Result<Boolean> save(@RequestBody SysDictItem sysDictItem) {
        return Result.ok(sysDictItemService.saveDictItem(sysDictItem));
    }

    /**
     * 修改字典项
     *
     * @param sysDictItem 字典项
     * @return R
     */
    @Operation(summary = "修改字典项", description = "修改字典项")
    @SysLog("修改字典项")
    @PutMapping("/item")
    @CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
    public Result<Boolean> updateById(@RequestBody SysDictItem sysDictItem) {
        return Result.ok(sysDictItemService.updateDictItem(sysDictItem));
    }

    /**
     * 通过id删除字典项
     *
     * @param id id
     * @return R
     */
    @Operation(summary = "删除字典项", description = "删除字典项")
    @Parameter(name = "id", description = "字典项ID")
    @SysLog("删除字典项")
    @DeleteMapping("/item/{id:\\d+}")
    @CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
    public Result<Boolean> removeDictItemById(@PathVariable Long id) {
        return Result.ok(sysDictItemService.removeDictItem(id));
    }

    @Operation(summary = "清除字典缓存", description = "清除字典缓存")
    @SysLog("清除字典缓存")
    @DeleteMapping("/cache")
    @PreAuthorize("@pms.hasPermission('sys_dict_del')")
    @CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
    public Result<Boolean> clearDictCache() {
        return Result.ok(sysDictService.clearDictCache());
    }

}
