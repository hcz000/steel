package com.gv.steel.common.redis;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.gv.steel.common.core.jackson.Java8TimeModule;
import com.gv.steel.common.core.lock.DistributedLock;
import com.gv.steel.common.redis.aspect.RepeatLimitAspect;
import com.gv.steel.common.redis.lock.RedissonDistributedLock;
import com.gv.steel.common.redis.properties.CacheManagerProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.ReadMode;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@EnableCaching
@AllArgsConstructor
@EnableConfigurationProperties({RedisProperties.class, CacheManagerProperties.class})
public class RedisAutoConfiguration {

    private final RedisProperties redisProperties;

    private final CacheManagerProperties cacheManagerProperties;

    @Bean
    @ConditionalOnClass({RedissonClient.class})
    public DistributedLock redissonDistributedLock(RedissonClient redissonClient) {
        return new RedissonDistributedLock(redissonClient);
    }


    @Primary
    @Bean
    @ConditionalOnExpression("${steel.redis.cluster:false}")
    public RedisConnectionFactory connectionFactory(GenericObjectPoolConfig<Object> redisPool) {
        RedisClusterConfiguration configuration = new RedisClusterConfiguration(redisProperties.getCluster().getNodes());
        configuration.setMaxRedirects(3);
        configuration.setPassword(redisProperties.getPassword());
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder().poolConfig(redisPool).build();
        return new LettuceConnectionFactory(configuration, clientConfiguration);
    }

    @Primary
    @Bean({"redisTemplate"})
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory, GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer) {
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setDefaultSerializer(redisSerializer);
        redisTemplate.setKeySerializer(redisSerializer);
        redisTemplate.setHashKeySerializer(redisSerializer);
        redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass({RedisProperties.class})
    public GenericObjectPoolConfig<Object> redisPool() {
        GenericObjectPoolConfig<Object> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxWaitMillis(redisProperties.getLettuce().getPool().getMaxActive());
        poolConfig.setMaxIdle(redisProperties.getLettuce().getPool().getMaxIdle());
        poolConfig.setMinIdle(redisProperties.getLettuce().getPool().getMinIdle());
        return poolConfig;
    }

    @Bean
    @Primary
    public GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer() {
        ObjectMapper om = new ObjectMapper();
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 反序列化时,遇到未知属性(那些没有对应的属性来映射的属性,并且没有任何setter或handler来处理这样的属性)时，是否引起结果失败(通过抛JsonMappingException异常)
        // 默认是启用的(意味着,如果遇到未知属性时会抛一个JsonMappingException)
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        om.registerModule(new Java8TimeModule());
        om.registerModule(new ParameterNamesModule());
        return new GenericJackson2JsonRedisSerializer(om);
    }

    @Bean
    public RedissonAutoConfigurationCustomizer redissonCustomizer(ObjectMapper objectMapper) {
        return (config) -> {
            if (Optional.ofNullable(redisProperties.getCluster()).isPresent()) {
                config.useClusterServers().setReadMode(ReadMode.MASTER);
            }
            config.setCodec(new JsonJacksonCodec(objectMapper));
            log.info("初始化 redisson 配置");
        };
    }

    @Bean("cacheManager")
    @Primary
    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        RedisCacheConfiguration difConf = this.getDefConf(redisSerializer, jdkSerializationRedisSerializer).entryTtl(Duration.ofHours(24L));
        int configSize = this.cacheManagerProperties.getConfigs() == null ? 0 : this.cacheManagerProperties.getConfigs().size();
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>(configSize);
        if (configSize > 0) {
            this.cacheManagerProperties.getConfigs().forEach((e) -> {
                RedisCacheConfiguration conf = this.getDefConf(redisSerializer, jdkSerializationRedisSerializer).entryTtl(Duration.ofSeconds(e.getSecond()));
                redisCacheConfigurationMap.put(e.getKey(), conf);
            });
        }
        return RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(difConf).withInitialCacheConfigurations(redisCacheConfigurationMap).build();
    }

    @Bean
    @ConditionalOnClass({RedissonClient.class})
    public RepeatLimitAspect repeatLimitAspect(DistributedLock distributedLock) {
        return new RepeatLimitAspect(distributedLock);
    }

    private RedisCacheConfiguration getDefConf(RedisSerializer<String> redisKeySerializer, JdkSerializationRedisSerializer jdkSerializationRedisSerializer) {
        return RedisCacheConfiguration
                .defaultCacheConfig()
                .disableCachingNullValues()
                .computePrefixWith((cacheName) -> "cache".concat(":").concat(cacheName).concat(":"))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisKeySerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jdkSerializationRedisSerializer));
    }

}
