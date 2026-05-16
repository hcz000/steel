package com.gv.steel.common.core.config;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gv.steel.common.core.format.SpecFieldAnnotationIntrospector;
import com.gv.steel.common.core.format.SpecFormatter;
import com.gv.steel.common.core.format.deserializer.SpecFieldDeserializer;
import com.gv.steel.common.core.format.serializer.SpecFieldSerializer;
import com.gv.steel.common.core.jackson.Java8TimeModule;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

/**
 * jackson configuration
 */
@AutoConfiguration
@ConditionalOnClass(ObjectMapper.class)
@AutoConfigureBefore(JacksonAutoConfiguration.class)
public class JacksonConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            builder.locale(Locale.CHINA);
            builder.timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
            builder.simpleDateFormat(DatePattern.NORM_DATETIME_PATTERN);
            builder.serializerByType(Long.class, ToStringSerializer.instance);
            builder.modules(new Java8TimeModule());
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public SpecFormatter createSpecFormatter() {
        return new SpecFormatter();
    }

    @Bean
    public SpecFieldSerializer specFieldSerializer(SpecFormatter specFormatter) {
        return new SpecFieldSerializer(specFormatter);
    }

    @Bean
    public SpecFieldDeserializer specFieldDeserializer(SpecFormatter specFormatter) {
        return new SpecFieldDeserializer(specFormatter);
    }

    @Bean
    public SpecFieldAnnotationIntrospector specFieldAnnotationIntrospector(SpecFormatter specFormatter) {
        return new SpecFieldAnnotationIntrospector(specFormatter);
    }

    @Bean
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder, SpecFieldAnnotationIntrospector introspector) {
        ObjectMapper mapper = builder.createXmlMapper(false).build();
        AnnotationIntrospector annotationIntrospector = mapper.getSerializationConfig().getAnnotationIntrospector();
        AnnotationIntrospector pair = AnnotationIntrospector.pair(annotationIntrospector, introspector);
        mapper.setAnnotationIntrospector(pair);
        return mapper;
    }
}
