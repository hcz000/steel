package com.gv.steel.common.feign;

import com.gv.steel.common.feign.config.FeignCommonHeaderInterceptorConfig;
import com.gv.steel.common.feign.interceptor.OkHttpLoggerInterceptor;
import com.gv.steel.common.feign.properties.RestTemplateProperties;
import feign.Client;
import lombok.AllArgsConstructor;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.commons.httpclient.OkHttpClientConnectionPoolFactory;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * sentinel 配置
 */
@AllArgsConstructor
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter({FeignAutoConfiguration.class})
@EnableConfigurationProperties({RestTemplateProperties.class})
@Import({FeignCommonHeaderInterceptorConfig.class})
public class CustomRestTemplateAutoConfiguration {

    private final RestTemplateProperties restTemplateProperties;

//    private final ConnectionPool connectionPool;

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(ClientHttpRequestFactory httpRequestFactory) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(httpRequestFactory);
        return restTemplate;
    }

    @Bean
    public ClientHttpRequestFactory httpRequestFactory(OkHttpClient okHttpClient) {
        return new OkHttp3ClientHttpRequestFactory(okHttpClient);
    }


    @Bean
    @ConditionalOnMissingBean({Client.class})
    public Client feignClient(OkHttpClient client) {
        return new feign.okhttp.OkHttpClient(client);
    }

    @Bean
    @ConditionalOnMissingBean({ConnectionPool.class})
    public ConnectionPool httpClientConnectionPool(FeignHttpClientProperties httpClientProperties, OkHttpClientConnectionPoolFactory connectionPoolFactory) {
        int maxConnections = httpClientProperties.getMaxConnections();
        long timeToLive = httpClientProperties.getTimeToLive();
        TimeUnit ttlUnit = httpClientProperties.getTimeToLiveUnit();
        return connectionPoolFactory.create(maxConnections, timeToLive, ttlUnit);
    }

    /**
     * OkHttp 客户端配置
     *
     * @return OkHttp 客户端配
     */
    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder().retryOnConnectionFailure(true)
                .connectTimeout(this.restTemplateProperties.getConnectTimeout(), TimeUnit.SECONDS) // 连接超时时间
                .readTimeout(this.restTemplateProperties.getReadTimeout(), TimeUnit.SECONDS) // 读取超时时间
                .writeTimeout(this.restTemplateProperties.getReadTimeout(), TimeUnit.SECONDS)
//                .connectionPool(connectionPool) // 使用连接池
                .addInterceptor(new OkHttpLoggerInterceptor())
                .followRedirects(true) // 是否允许重定向
                .build();
    }
}
