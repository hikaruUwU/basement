package com.demo.base.component;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
@SuppressWarnings("unused")
public class RequestChain implements HandlerInterceptor, WebMvcConfigurer {
    private static final ThreadLocal<Map<Object, Object>> local = ThreadLocal.withInitial(HashMap::new);

    public static void put(Object key, Object value) {
        local.get().put(key, value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Object key) {
        return (T) local.get().get(key);
    }

    public static Map<Object, Object> peek() {
        return Collections.unmodifiableMap(local.get());
    }

    public static void truncate() {
        local.remove();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this).addPathPatterns("/**");
    }

    @Override
    public void afterCompletion(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler, Exception ex) {
        RequestChain.local.remove();
    }
}