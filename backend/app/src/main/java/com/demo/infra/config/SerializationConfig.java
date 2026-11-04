package com.demo.infra.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.fasterxml.jackson.module.blackbird.BlackbirdModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 全部 HTTP 载荷的 Jackson 默认配置:Blackbird 与 Afterburner 字节码加速,
 * JSR-310 日期序列化为 ISO 字符串而非数值时间戳,{@code null} 字段省略,
 * 入站未知属性忽略,空 bean 容忍。
 * <p>
 * Jackson defaults for all HTTP payloads: Blackbird and Afterburner bytecode acceleration,
 * JSR-310 dates rendered as ISO strings rather than numeric timestamps, {@code null} fields
 * omitted, unknown inbound properties ignored, and empty beans tolerated.
 */
@Configuration
public class SerializationConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> builder
                .modules(
                        new BlackbirdModule(),
                        new AfterburnerModule(),
                        new JavaTimeModule()
                )
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .featuresToDisable(
                        SerializationFeature.FAIL_ON_EMPTY_BEANS,
                        SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
                        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                        MapperFeature.DEFAULT_VIEW_INCLUSION
                );
    }
}