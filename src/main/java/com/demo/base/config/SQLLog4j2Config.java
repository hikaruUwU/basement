package com.demo.base.config;

import com.mybatisflex.core.audit.AuditManager;
import com.mybatisflex.core.audit.AuditMessage;
import com.mybatisflex.core.audit.MessageReporter;
import com.mybatisflex.core.audit.ScheduledMessageCollector;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.List;

@Log4j2
public class SQLLog4j2Config implements MessageReporter {

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        AuditManager.setAuditEnable(true);
        AuditManager.setMessageCollector(new ScheduledMessageCollector());
        AuditManager.setMessageReporter(this);

        log.info("SQL Audit enabled.");
    }

    @Override
    public void sendMessages(List<AuditMessage> list) {
        for (AuditMessage message : list) {
            long elapsedTime = message.getElapsedTime();
            if (elapsedTime > 256L)
                log.warn(">>> SQL > {} >> [{} ms]", message::getFullSql, () -> elapsedTime);
            else
                log.info(">>> SQL > {} >> [{} ms]", message::getFullSql, () -> elapsedTime);
        }
    }
}