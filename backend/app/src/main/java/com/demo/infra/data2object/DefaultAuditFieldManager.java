package com.demo.infra.data2object;

import com.demo.infra.core.context.SessionContext;
import com.mybatisflex.annotation.InsertListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Fallback;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

@Component
public class DefaultAuditFieldManager {
    @Fallback
    @Bean
    public InsertListener fallback(SessionContext sessionContext){
        return entity -> {
            if (sessionContext.hasSession() && ClassUtils.isAssignable(AuditField.class, entity.getClass()))
                ((AuditField) entity).setCreator((sessionContext.getIdentifier()));
        };
    }
}