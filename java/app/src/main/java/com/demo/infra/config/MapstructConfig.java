package com.demo.infra.config;

import org.mapstruct.MapperConfig;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * MapStruct 共享配置,经 {@code @Mapper(config = MapstructConfig.class)} 应用:
 * Spring 组件模型、目标属性未映射即构建失败、源属性未映射忽略、
 * 有损类型转换告警、已配置映射自动继承。
 * <p>
 * Shared MapStruct settings, applied via {@code @Mapper(config = MapstructConfig.class)}:
 * Spring component model, unmapped target properties fail the build, unmapped sources are
 * ignored, lossy type conversions warn, and configured mappings are inherited automatically.
 */
@MapperConfig(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        typeConversionPolicy = ReportingPolicy.WARN,
        mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_ALL_FROM_CONFIG
)
public interface MapstructConfig {
}