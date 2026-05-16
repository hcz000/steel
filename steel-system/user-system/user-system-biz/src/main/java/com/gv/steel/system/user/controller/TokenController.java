package com.gv.steel.system.user.controller;

import com.gv.steel.common.core.api.Result;
import com.gv.steel.system.user.feign.RemoteTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
@Tag(name = "令牌管理模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class TokenController {

    private final RemoteTokenService remoteTokenService;

    /**
     * 分页token 信息
     *
     * @param params 参数集
     * @return token集合
     */
    @Operation(summary = "分页token 信息", description = "分页token 信息")
    @GetMapping("/page")
    public Result token(@RequestParam Map<String, Object> params) {
        return remoteTokenService.getTokenPage(params);
    }

    /**
     * 删除
     *
     * @param id ID
     * @return success/false
     */
    @Operation(summary = "删除token 信息", description = "删除token 信息")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('sys_token_del')")
    public Result<Boolean> delete(@PathVariable String id) {
        return remoteTokenService.removeToken(id);
    }

}
