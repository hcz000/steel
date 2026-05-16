package com.gv.steel.gateway.handler;

import com.gv.steel.common.core.constant.CacheConstants;
import com.gv.steel.common.core.constant.SecurityConstants;
import com.gv.steel.common.redis.utils.RedisUtil;
import com.wf.captcha.SpecCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Optional;

@Slf4j
public class CaptchaImageHandler implements HandlerFunction<ServerResponse> {

    private static final Integer DEFAULT_IMAGE_WIDTH = 100;

    private static final Integer DEFAULT_IMAGE_HEIGHT = 40;

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        SpecCaptcha captcha = new SpecCaptcha(DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT, SecurityConstants.CODE_SIZE);
        String result = captcha.text();
        Optional<String> randomStr = request.queryParam("randomStr");
        randomStr.ifPresent(s -> {
            RedisUtil.setCacheObject(CacheConstants.DEFAULT_CODE_KEY + s, result, Duration.ofSeconds(SecurityConstants.CODE_TIME));
        });
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        captcha.out(os);
        return ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_JPEG)
                .body(BodyInserters.fromResource(new ByteArrayResource(os.toByteArray())));
    }
}
