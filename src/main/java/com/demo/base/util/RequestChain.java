package com.demo.base.util;

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

    public static void putAll(Map<?, ?> map) {
        local.get().putAll(map);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Object key) {
        return (T) local.get().get(key);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getOrDefault(Object key, T defaultValue) {
        return (T) local.get().getOrDefault(key, defaultValue);
    }

    @SuppressWarnings("unchecked")
    public static <K,V> Map<K,V> peek() {
        return (Map<K, V>) Collections.unmodifiableMap(local.get());
    }

    public static void truncate() {
        local.remove();
        local.set(new HashMap<>());
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