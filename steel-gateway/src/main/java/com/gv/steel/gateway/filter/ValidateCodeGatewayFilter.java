package com.gv.steel.gateway.filter;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.core.constant.CacheConstants;
import com.gv.steel.common.core.constant.CommonConstants;
import com.gv.steel.common.core.constant.ErrorCodeConstants;
import com.gv.steel.common.core.constant.SecurityConstants;
import com.gv.steel.common.core.exception.ValidateCodeException;
import com.gv.steel.common.core.util.MsgUtils;
import com.gv.steel.common.core.util.WebUtils;
import com.gv.steel.common.redis.utils.RedisUtil;
import com.gv.steel.gateway.properties.GatewayConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Locale;
import java.util.Optional;


/**
 * 验证码处理
 */
@Slf4j
@RequiredArgsConstructor
public class ValidateCodeGatewayFilter extends AbstractGatewayFilterFactory<Object> {

    private final GatewayConfigProperties gatewayConfigProperties;

    private final ObjectMapper objectMapper;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            try {
                ServerHttpRequest request = exchange.getRequest();
                HttpHeaders headers = request.getHeaders();
                String lang = Optional.ofNullable(headers.get(CommonConstants.HEADER_LANG))
                        .orElse(Lists.newArrayList()).stream()
                        .findFirst().orElse(Locale.SIMPLIFIED_CHINESE.toLanguageTag());
                List<Locale.LanguageRange> list = Locale.LanguageRange.parse(lang);
                String langTag = Locale.lookupTag(list, gatewayConfigProperties.getLanguages());
                Locale locale = Locale.forLanguageTag(langTag);
                LocaleContextHolder.setLocaleContext(new SimpleLocaleContext(locale));
                boolean isAuthToken = CharSequenceUtil.containsAnyIgnoreCase(request.getURI().getPath(),
                        SecurityConstants.OAUTH_TOKEN_URL);

                // 不是登录请求，直接向下执行
                if (!isAuthToken) {
                    return chain.filter(exchange);
                }

                // 刷新token，手机号登录（也可以这里进行校验） 直接向下执行
                String grantType = request.getQueryParams().getFirst("grant_type");
                if (StrUtil.equals(SecurityConstants.REFRESH_TOKEN, grantType)) {
                    return chain.filter(exchange);
                }

                boolean isIgnoreClient = gatewayConfigProperties.getIgnoreClients().contains(WebUtils.getClientId(request));
                try {
                    // only oauth and the request not in ignore clients need check code.
                    if (!isIgnoreClient) {
                        checkCode(request);
                    }
                } catch (Exception e) {
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.PRECONDITION_REQUIRED);
                    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

                    final String errMsg = e.getMessage();
                    return response.writeWith(Mono.create(monoSink -> {
                        try {
                            byte[] bytes = objectMapper.writeValueAsBytes(Result.failed(errMsg));
                            DataBuffer dataBuffer = response.bufferFactory().wrap(bytes);

                            monoSink.success(dataBuffer);
                        } catch (JsonProcessingException jsonProcessingException) {
                            log.error("对象输出异常", jsonProcessingException);
                            monoSink.error(jsonProcessingException);
                        }
                    }));
                }
                return chain.filter(exchange);
            } finally {
                LocaleContextHolder.resetLocaleContext();
            }
        };
    }

    @SneakyThrows
    private void checkCode(ServerHttpRequest request) {
        String code = request.getQueryParams().getFirst("code");

        if (StrUtil.isBlank(code)) {
            throw new ValidateCodeException(MsgUtils.getSystemMessage(ErrorCodeConstants.VALIDATE_CODE_NOT_EMPTY));
        }

        String randomStr = request.getQueryParams().getFirst("randomStr");
        if (CharSequenceUtil.isBlank(randomStr)) {
            randomStr = request.getQueryParams().getFirst(SecurityConstants.SMS_PARAMETER_NAME);
        }

        String key = CacheConstants.DEFAULT_CODE_KEY + randomStr;

        String codeStr = RedisUtil.getCacheObject(key);

        if (ObjectUtil.isEmpty(codeStr)) {
            throw new ValidateCodeException(MsgUtils.getSystemMessage(ErrorCodeConstants.VALIDATE_CODE_EXPIRED));
        }

        if (!code.equalsIgnoreCase(codeStr)) {
            throw new ValidateCodeException(MsgUtils.getSystemMessage(ErrorCodeConstants.VALIDATE_CODE_ERROR));
        }

        RedisUtil.deleteObject(key);
    }
}
