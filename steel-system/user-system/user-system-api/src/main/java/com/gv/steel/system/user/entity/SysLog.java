package com.gv.steel.system.user.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gv.steel.common.mybatis.base.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 日志表
 * </p>
 */
@Data
@TableName("sys_log")
@Schema(description = "系统日志")
@EqualsAndHashCode(callSuper = true)
public class SysLog extends BaseEntity<SysLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 日志类型
     */
    @NotNull(message = "日志类型不能为空")
    @Schema(description = "日志类型")
    private Integer type;

    /**
     * 日志标题
     */
    @NotBlank(message = "日志标题不能为空")
    @Schema(description = "日志标题")
    private String title;

    /**
     * 操作IP地址
     */
    @Schema(description = "操作ip地址")
    private String remoteAddr;

    /**
     * 操作IP所属地
     */
    @Schema(description = "操作IP所属地")
    private String remoteAddrRegion;

    /**
     * 用户浏览器
     */
    @Schema(description = "用户代理")
    private String userAgent;

    /**
     * 请求URI
     */
    @Schema(description = "请求uri")
    private String requestUri;

    /**
     * 操作方式
     */
    @Schema(description = "操作方式")
    private String method;

    /**
     * 操作提交的数据
     */
    @Schema(description = "数据")
    private String params;

    /**
     * 执行时间
     */
    @Schema(description = "方法执行时间")
    private Long time;

    /**
     * 异常信息
     */
    @Schema(description = "异常信息")
    private String exception;

    /**
     * 服务ID
     */
    @Schema(description = "应用标识")
    private String serviceId;

    /**
     * 创建人名称
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人名称")
    private String createName;

    /**
     * 修改人名称
     */
    @Schema(description = "修改人名称")
    private String updateName;
}
