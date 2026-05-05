package com.demo.base.config;

import jakarta.annotation.PreDestroy;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.*;

@Log4j2
@UtilityClass
public class GlobalWarmUpManager {
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
            log.warn("PreHeat Task overlimited, some are discarded");
        }

        public static void trigger() {
        }
    }
}