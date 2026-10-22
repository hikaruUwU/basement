package com.demo.infra.config;

import com.mybatisflex.core.audit.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.concurrent.*;

@Log4j2
@Configuration
public class SQLLog4j2Config implements MessageReporter {
    private final boolean isProduction;

    private long lastCollectedClock = System.currentTimeMillis();

    private final MessageReporter thisReporter = this;

    public SQLLog4j2Config(Environment environment) {
        isProduction = Arrays.asList(environment.getActiveProfiles()).contains("prod");
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        AuditManager.setMessageReporter(thisReporter);
        AuditManager.setAuditEnable(true);
        AuditManager.setMessageCollector(isProduction ? new MessageCollector() {
            private final BlockingQueue<AuditMessage> queue = new LinkedBlockingQueue<>();
            private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
                Thread thread = new Thread(r);
                thread.setName("sqlAuditor");
                thread.setDaemon(true);
                thread.setPriority(Thread.MIN_PRIORITY);
                return thread;
            });

            {
                this.scheduler.scheduleWithFixedDelay(() -> {
                    try {
                        if (queue.isEmpty())
                            return;

                        List<AuditMessage> batch = new ArrayList<>();
                        queue.drainTo(batch);

                        thisReporter.sendMessages(batch);
                    } catch (Exception e) {
                        log.error("Error occurred while processing SQL audit messages", e);
                    }
                }, 60, 60, TimeUnit.SECONDS);
            }

            @Override
            public void collect(AuditMessage message) {
                var ignored = queue.offer(message);
            }
        } : (message) -> thisReporter.sendMessages(List.of(message)));
    }

    @Override
    public void sendMessages(List<AuditMessage> list) {
        if (isProduction)
            production(list);
        else
            development(list);
    }

    public void development(List<AuditMessage> list) {
        for (AuditMessage message : list) {
            var elapsedTime = message.getElapsedTime();
            if (elapsedTime > 256L)
                log.warn(">>> SQL > {} >> [{} ms]", message::getFullSql, () -> elapsedTime);
            else
                log.info(">>> SQL > {} >> [{} ms]", message::getFullSql, () -> elapsedTime);
        }
    }

    public void production(List<AuditMessage> list) {
        if (list == null || list.isEmpty()) {
            return;
        }

        List<Long> elapsed = list.stream()
                .map(AuditMessage::getElapsedTime)
                .sorted()
                .toList();

        LongSummaryStatistics stats = list.stream()
                .mapToLong(AuditMessage::getElapsedTime)
                .summaryStatistics();

        int size = elapsed.size();

        double median = (size % 2 == 0)
                ? (elapsed.get(size / 2 - 1) + elapsed.get(size / 2)) / 2.0
                : elapsed.get(size / 2);

        int p95Index = Math.min((int) Math.ceil(size * 0.95) - 1, size - 1);

        p95Index = Math.max(p95Index, 0);

        long p95 = elapsed.get(p95Index);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").withZone(ZoneId.systemDefault());

        log.info("""
                        Database Query Performance Report
                        
                        Total Queries Executed: {}
                        Average Execution Time: {}
                        Maximum Execution Time: {}
                        Minimum Execution Time: {}
                        Median  Execution Time: {}
                        P95     Execution Time: {}
                        
                        Unit:  millisecond
                        Range: {} -> {}
                        EVAL:  {}
                        """,
                stats.getCount(),
                String.format("%.2f", stats.getAverage()),
                stats.getMax(),
                stats.getMin(),
                String.format("%.1f", median),
                p95,
                formatter.format(Instant.ofEpochMilli(lastCollectedClock)),
                formatter.format(Instant.now()),
                p95 > 128 ? "ALERT" : "HEALTHY"
        );

        lastCollectedClock = System.currentTimeMillis();
    }
}