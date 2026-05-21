package com.demo.base;

import com.demo.base.annotation.detective.EnableSqlLighthouse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@EnableLoadTimeWeaving(aspectjWeaving = EnableLoadTimeWeaving.AspectJWeaving.AUTODETECT)
@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
@EnableSqlLighthouse
public class BaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(BaseApplication.class, args);
    }

    static {
        if ("prod".equalsIgnoreCase(System.getProperty("spring.profiles.active"))) {
            System.setProperty("log4j2.contextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
            System.setProperty("log4j2.AsyncQueueFullPolicy", "Discard");
            System.setProperty("log4j2.DiscardThreshold", "INFO");
        }
    }
}