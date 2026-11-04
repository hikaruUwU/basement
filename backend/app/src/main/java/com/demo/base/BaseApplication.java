package com.demo.base;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Log4j2
@SpringBootApplication(scanBasePackages = {
        "com.demo.base",
        "com.demo.infra",
})
@EnableTransactionManagement
@EnableScheduling
public class BaseApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(BaseApplication.class, args);
        } catch (Exception e) {
            log.fatal(e);
            Runtime.getRuntime().exit(1);
        }
    }

    static {
        Environment env = new StandardEnvironment();

        System.out.print("Running on " + (env.acceptsProfiles(Profiles.of("prod")) ? "prod" : "dev"));

        if ("prod".equalsIgnoreCase(System.getProperty("spring.profiles.active"))) {
            System.setProperty("log4j2.contextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
            System.setProperty("log4j2.AsyncQueueFullPolicy", "Discard");
            System.setProperty("log4j2.DiscardThreshold", "INFO");
        }
    }
}