package com.demo.base.config;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.*;

@Log4j2
@UtilityClass
public class GlobalWarmUpManager {
    public static final ExecutorService executor = warmupExecutor();
    private static ExecutorService warmupExecutor() {
        return new ThreadPoolExecutor(
                0,
                Runtime.getRuntime().availableProcessors(),
                16L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(64),
                Thread.ofVirtual().name("warmupWorker-", 0L).factory(),
                (r, executor) -> Notify.trigger()
        );
    }

    private class Notify {
        static {
            log.warn("PreHeat Task overlimited, some are discarded");
        }

        public static void trigger() {
        }
    }
}