package com.gv.steel.system.user.service;


import com.gv.steel.common.core.api.Result;
import com.gv.steel.system.user.dto.AppSmsDTO;

public interface AppService {

    /**
     * 发送手机验证码
     *
     * @param sms phone
     * @return code
     */
    Result<Boolean> sendSmsCode(AppSmsDTO sms);

    /**
     * 校验验证码
     *
     * @param phone 手机号
     * @param code  验证码
     * @return
     */
    boolean check(String phone, String code);

}
