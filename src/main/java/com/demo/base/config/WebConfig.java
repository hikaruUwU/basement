package com.demo.base.config;

import io.undertow.UndertowOptions;
import io.undertow.server.DefaultByteBufferPool;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import jakarta.annotation.Nonnull;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Configuration
public class WebConfig implements WebMvcConfigurer, WebServerFactoryCustomizer<UndertowServletWebServerFactory> {
    @Bean
    public ThreadFactory threadFactory() {
        return Thread.ofVirtual().factory();
    }

    @Override
    public void addCorsMappings(@Nonnull CorsRegistry registry) {
    }

    @Bean
    public WebServerFactoryCustomizer<UndertowServletWebServerFactory> webServerFactoryCustomizer() {
        return factory -> factory.addDeploymentInfoCustomizers(deploymentInfo -> {
            deploymentInfo.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
            deploymentInfo.setAsyncExecutor(Executors.newVirtualThreadPerTaskExecutor());
        });
    }

    @Override
    public void customize(UndertowServletWebServerFactory factory) {
        factory.addDeploymentInfoCustomizers(deploymentInfo -> {
            WebSocketDeploymentInfo webSocketDeploymentInfo = new WebSocketDeploymentInfo();
            webSocketDeploymentInfo.setBuffers(new DefaultByteBufferPool(true, 16384, -1, 12));
            deploymentInfo.addServletContextAttribute(WebSocketDeploymentInfo.ATTRIBUTE_NAME, webSocketDeploymentInfo);
        });
        factory.addBuilderCustomizers(builder -> {
            builder.setServerOption(UndertowOptions.MAX_ENTITY_SIZE, 10485760L);
            builder.setServerOption(UndertowOptions.NO_REQUEST_TIMEOUT, 60000);
        });
    }
}