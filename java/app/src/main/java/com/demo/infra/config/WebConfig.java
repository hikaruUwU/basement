package com.demo.infra.config;

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

/**
 * Undertow 与 MVC 调优:servlet 与异步任务运行在 per-task 虚拟线程上,
 * WebSocket 缓冲取自池化直接内存分配器,请求实体上限 10 MiB,空闲连接 60 s 断开。
 * 未注册任何 CORS 映射 —— 部署形态为前端反向代理之后的同源。
 * <p>
 * Undertow and MVC tuning: servlet and async work run on per-task virtual threads, WebSocket
 * buffers come from a pooled direct allocator, request entities are capped at 10 MiB, and idle
 * connections are dropped after 60 s. No CORS mappings are registered — deployment is
 * same-origin behind the frontend reverse proxy.
 */
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