package com.gv.steel.system.user.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.core.util.MsgUtils;
import com.gv.steel.common.core.vo.UserInfo;
import com.gv.steel.common.security.annotation.Inner;
import com.gv.steel.system.user.constant.ErrorCodeConstants;
import com.gv.steel.system.user.dto.AppSmsDTO;
import com.gv.steel.system.user.entity.SysUser;
import com.gv.steel.system.user.service.AppService;
import com.gv.steel.system.user.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 移动端登录
 */
@RestController
@AllArgsConstructor
@RequestMapping("/app")
@Tag(name = "移动端登录模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class AppController {

    private final AppService appService;

    private final SysUserService userService;

    /**
     * 发送手机验证码
     *
     * @param sms 请求手机对象
     * @return code
     */
    @Operation(summary = "发送手机验证码")
    @Inner(value = false)
    @PostMapping("/sms")
    public Result<Boolean> sendSmsCode(@Valid @RequestBody AppSmsDTO sms) {
        return appService.sendSmsCode(sms);
    }

    /**
     * 获取指定用户全部信息
     *
     * @param phone 手机号
     * @return 用户信息
     */
    @Operation(summary = "获取指定用户全部信息")
    @Parameter(name = "phone", description = "手机号", required = true)
    @Inner
    @GetMapping("/info/{phone}")
    public Result<UserInfo> infoByMobile(@PathVariable String phone) {
        SysUser user = userService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getPhone, phone));
        if (user == null) {
            return Result.failed(MsgUtils.getMessage(ErrorCodeConstants.SYS_USER_USERINFO_EMPTY, phone));
        }
        return Result.ok(userService.getUserInfo(user));
    }

}
