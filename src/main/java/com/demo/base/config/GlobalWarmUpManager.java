package com.demo.base.config;

import jakarta.annotation.PreDestroy;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Log4j2
public abstract class GlobalWarmUpManager {
    public static final ExecutorService executor = warmupExecutor();

    @PreDestroy
    public void destroy(){
        executor.shutdownNow();
    }

    private static ExecutorService warmupExecutor() {
        return new ThreadPoolExecutor(
                0,
                Runtime.getRuntime().availableProcessors(),
                8L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(64),
                Thread.ofVirtual().name("preHeat-", 0L).factory(),
                (r, executor) -> Notify.trigger()
        );
    }

    private static class Notify {
        static {
            log.warn("PreHeat Task queue overlimited, some task will be discarded");
        }

        public static void trigger() {
        }
    }
}