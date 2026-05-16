package com.gv.steel.system.base.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.system.base.entity.AnnealModel;
import com.gv.steel.system.base.service.AnnealModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 退火模型表 前端控制器
 * </p>
 *
 * @author administrator
 * @since 2025-05-12
 */
@Tag(name = "退火模型表接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/anneal-model")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class AnnealModelController {

	private final AnnealModelService annealModelService;

	@GetMapping("/getByCode/{code}")
	public Result<AnnealModel> getByCode(@PathVariable String code) {
		return Result.ok(annealModelService.getOne(Wrappers.<AnnealModel>lambdaQuery().eq(AnnealModel::getCode, code)));
	}

	/**
	 * 退火模型表新增
	 */
	@Operation(summary = "退火模型表新增")
	@PostMapping
	public Result<Boolean> save(@RequestBody @Valid AnnealModel annealModel) {
		return Result.ok(annealModelService.saveAnnealModel(annealModel));
	}

	/**
	 * 退火模型表修改
	 */
	@Operation(summary = "退火模型表修改")
	@PutMapping
	public Result<Boolean> update(@RequestBody @Valid AnnealModel annealModel) {
		return Result.ok(annealModelService.updateAnnealModelById(annealModel));
	}

	/**
	 * 退火模型表删除
	 */
	@Operation(summary = "退火模型表删除", hidden = true)
	@Parameter(name = "id", description = "退火模型表ID", required = true)
	@DeleteMapping("/{id:\\d+}")
	public Result<Boolean> delete(@PathVariable Long id) {
		return Result.ok(annealModelService.removeById(id));
	}
}
