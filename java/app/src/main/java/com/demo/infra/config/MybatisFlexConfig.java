package com.demo.infra.config;

import com.demo.infra.data2object.AuditField;
import com.mybatisflex.annotation.InsertListener;
import com.mybatisflex.core.FlexGlobalConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MybatisFlexConfig {
    private final InsertListener auditFieldManager;

    @PostConstruct
    public void init() {
        FlexGlobalConfig.getDefaultConfig().registerInsertListener(auditFieldManager, AuditField.class);
    }
}
