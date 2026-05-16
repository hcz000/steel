package com.gv.steel.system.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 客户端请求验证码
 */
@Data
@Schema(description = "APP短信验证码DTO")
public class AppSmsDTO {

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    @NotBlank(message = "手机号不能为空")
    private String phone;

    /**
     * 手机号是否存在数据库
     */
    @Schema(description = "手机号是否存在数据库")
    private Boolean exist;

}
