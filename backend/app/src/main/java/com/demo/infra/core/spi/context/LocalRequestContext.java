package com.demo.infra.core.spi.context;

import com.demo.infra.core.context.RequestContext;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Fallback;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.Map;

@Fallback
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class LocalRequestContext<K, V> implements RequestContext<K, V>, HandlerInterceptor, WebMvcConfigurer {
    private final ThreadLocal<Map<K, V>> local = ThreadLocal.withInitial(HashMap::new);

    public void put(K key, V value) {
        local.get().put(key, value);
    }

    public V get(K key) {
        return local.get().get(key);
    }

    @Override
    public void destroy() {
        local.remove();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this).addPathPatterns("/**");
    }

    @Override
    public void afterCompletion(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler, Exception ex) {
        destroy();
    }
}