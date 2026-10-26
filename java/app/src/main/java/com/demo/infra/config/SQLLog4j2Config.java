package com.demo.infra.config;

import com.mybatisflex.core.audit.AuditManager;
import com.mybatisflex.core.audit.AuditMessage;
import com.mybatisflex.core.audit.MessageReporter;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;

import java.util.List;

@Log4j2
@Configuration
@Profile("dev")
public class SQLLog4j2Config implements MessageReporter {
    private final MessageReporter thisReporter = this;

    public SQLLog4j2Config() {
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        AuditManager.setMessageReporter(thisReporter);
        AuditManager.setAuditEnable(true);
        AuditManager.setMessageCollector(message -> thisReporter.sendMessages(List.of(message)));
    }

    @Override
    public void sendMessages(List<AuditMessage> list) {
        for (AuditMessage message : list) {
            var elapsedTime = message.getElapsedTime();

            if (elapsedTime > 256L) {
                log.warn(">>> SQL > {} >> [{} ms]",
                        message::getFullSql, () -> elapsedTime);
            } else {
                log.info(">>> SQL > {} >> [{} ms]",
                        message::getFullSql, () -> elapsedTime);
            }
        }
    }
}