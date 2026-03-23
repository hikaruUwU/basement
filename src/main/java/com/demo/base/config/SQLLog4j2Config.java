package com.demo.base.config;

import com.mybatisflex.core.audit.AuditManager;
import com.mybatisflex.core.audit.AuditMessage;
import com.mybatisflex.core.audit.MessageReporter;
import com.mybatisflex.core.audit.ScheduledMessageCollector;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
public class SQLLog4j2Config implements MessageReporter {

    @PostConstruct
    public void init() {
        AuditManager.setAuditEnable(true);
        AuditManager.setMessageCollector(new ScheduledMessageCollector());
        AuditManager.setMessageReporter(this);
    }

    @Override
    public void sendMessages(List<AuditMessage> list) {
        for (AuditMessage message : list)
            if (message.getElapsedTime() > 256L)
                log.warn(">>> SLOW SQL PLAN > {} >> [{} ms]", message.getFullSql(), message.getElapsedTime());
            else
                log.info(">>> SQL PLAN > {} >> [{} ms]", message.getFullSql(), message.getElapsedTime());
    }
}