package com.demo.base.util;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class TransactionRunner {
    private final static ExecutorService virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();

    private final TransactionTemplate transactionTemplate;

    @PreDestroy
    public void destroy() {
        virtualThreadExecutor.shutdown();
    }

    public <T> T runSynchronously(Supplier<T> supplier) {
        return transactionTemplate.execute(status -> supplier.get());
    }

    public CompletableFuture<Void> runAsync(Runnable runnable) {
        return CompletableFuture.runAsync(() -> transactionTemplate.execute(status -> {
            try {
                runnable.run();
                return null;
            } catch (Exception e) {
                status.setRollbackOnly();
                throw e;
            }
        }), virtualThreadExecutor);
    }
}