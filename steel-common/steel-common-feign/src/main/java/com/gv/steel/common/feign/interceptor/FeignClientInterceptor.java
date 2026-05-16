package com.gv.steel.common.feign.interceptor;

import com.gv.steel.common.core.util.WebUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class FeignClientInterceptor implements RequestInterceptor {
    private final static String[] TRANSFER_HEADER = {"accept-language", "Accept-Language"};

    @Override
    public void apply(RequestTemplate template) {
        WebUtils.getRequest().ifPresent(request -> {
            for (String headName : TRANSFER_HEADER) {
                Optional.ofNullable(request.getHeader(headName)).ifPresent(
                        headValue -> template.header(headName, headValue)
                );
            }
//            Iterator<String> iterator = request.getHeaderNames().asIterator();
//            while (iterator.hasNext()) {
//                String headName = iterator.next();
//                String header = request.getHeader(headName);
//                template.header(headName, header);
//                if (log.isDebugEnabled()) {
//                    log.debug("Pass request headers through feign: {} - {}", headName, header);
//                }
//            }
        });
    }
}
