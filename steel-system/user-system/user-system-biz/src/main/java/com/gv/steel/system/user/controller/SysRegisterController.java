package com.gv.steel.system.user.controller;

import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.log.annotation.SysLog;
import com.gv.steel.common.security.annotation.Inner;
import com.gv.steel.system.user.dto.UserDTO;
import com.gv.steel.system.user.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 客户端注册功能 register.user = false
 */
@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
@ConditionalOnProperty(name = "register.user", matchIfMissing = true)
public class SysRegisterController {

    private final SysUserService userService;

    /**
     * 注册用户
     *
     * @param userDto 用户信息
     * @return success/false
     */
    @Operation(summary = "注册用户", description = "注册用户")
    @Inner(value = false)
    @SysLog("注册用户")
    @PostMapping("/user")
    public Result<Boolean> registerUser(@RequestBody UserDTO userDto) {
        return userService.registerUser(userDto);
    }

}
