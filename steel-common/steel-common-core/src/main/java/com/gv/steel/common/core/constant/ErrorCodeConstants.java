package com.gv.steel.common.core.constant;

public interface ErrorCodeConstants {
    /*-------------------result msg-----------------*/
    String DEFAULT_SUCCESS_MSG = "default.success.msg";
    String DEFAULT_FAIL_MSG = "default.fail.msg";
    /*-------------------result msg-----------------*/

    /*-------------------exception msg--------------------*/
    /**
     * 默认异常信息
     */
    String DEFAULT_EXCEPTION = "default.exception";
    /**
     * 服务拒绝访问
     */
    String SERVER_ACCESS_DENIED = "server.access.denied";
    /**
     * 不支持的媒体类型
     */
    String HTTP_MEDIA_TYPE_NOT_SUPPORTED = "http.media.type.not.supported";
    /**
     * 404 not found
     */
    String HTTP_NOT_FOUND = "http.not.found";
    /**
     * 数据库服务异常
     */
    String SQL_EXCEPTION = "sql.exception";
    /**
     * 请求方式异常
     */
    String REQUEST_METHOD_EXCEPTION = "request.method.exception";
    /**
     * 请求参数缺失或异常
     */
    String REQUIRED_PARAMETER_MISSING = "required.parameter.missing";
    /**
     * 没有可用的实例
     */
    String NO_INSTANCE_AVAILABLE = "not.instance.available";
    /**
     * 验证码不能为空
     */
    String VALIDATE_CODE_NOT_EMPTY = "validate.code.not.empty";
    /**
     * 验证码不合法
     */
    String VALIDATE_CODE_ERROR = "validate.code.error";
    /**
     * 验证码已失效
     */
    String VALIDATE_CODE_EXPIRED = "validate.code.expired";
    /**
     * 规格格式错误
     */
    String SPEC_FORMAT_ERROR = "spec.format.error";
    /*-------------------exception msg--------------------*/
}
