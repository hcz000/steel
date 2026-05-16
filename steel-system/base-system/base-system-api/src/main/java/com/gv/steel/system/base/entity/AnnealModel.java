package com.gv.steel.system.base.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gv.steel.common.mybatis.base.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 退火模型表
 * </p>
 *
 * @author administrator
 * @since 2025-05-12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_anneal_model")
@Schema(name = "AnnealModel对象", description = "退火模型表")
public class AnnealModel extends BaseEntity<AnnealModel> {

	private static final long serialVersionUID = 1L;

	@Schema(description = "模型标识")
	private String code;

	@Schema(description = "模型内容")
	private String value;


	@Override
	public Serializable pkVal() {
		return this.getId();
	}

}
