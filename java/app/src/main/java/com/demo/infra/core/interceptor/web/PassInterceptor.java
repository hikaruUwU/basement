package com.demo.infra.core.interceptor.web;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Log4j2
@Configuration
@RequiredArgsConstructor
public class PassInterceptor implements HandlerInterceptor, WebMvcConfigurer {
    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        //registry.addInterceptor(this).addPathPatterns("/**").order(WebInterceptorOrder.PASS.getOrder());
    }
}
