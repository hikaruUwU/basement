package com.demo.base.config;

import lombok.experimental.UtilityClass;

import java.util.concurrent.*;

@UtilityClass
public class GlobalWarmUpManager {
    public static final ExecutorService executor = warmupExecutor();

    private static ExecutorService warmupExecutor() {
        final int nThreads = Runtime.getRuntime().availableProcessors();

        ThreadFactory factory = Thread.ofVirtual()
                .name("warmup-worker-", 0L)
                .factory();

        return new ThreadPoolExecutor(
                0,
                nThreads,
                16L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(256),
                factory,
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
}