package com.gv.steel.common.feign.interceptor;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

@Slf4j
public class OkHttpLoggerInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        String logInfo = "okhttp request info: {}";
        Request request = chain.request();
        log.info(logInfo, request);
        return chain.proceed(chain.request());
    }
}
