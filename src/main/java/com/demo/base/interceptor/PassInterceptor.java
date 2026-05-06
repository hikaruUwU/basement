package com.demo.base.interceptor;

import com.demo.base.annotation.requireSession.RequiredSession;
import com.demo.base.config.GlobalWarmUpManager;
import com.demo.base.exception.UnauthenticatedAccessException;
import com.demo.base.util.ASMAnnotationScanner;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@Log4j2
@Configuration
@RequiredArgsConstructor
public class PassInterceptor implements HandlerInterceptor, WebMvcConfigurer {
    private final ASMAnnotationScanner asmAnnotationScanner;
    private final UnauthenticatedAccessException $ACCESS_DENIED = new UnauthenticatedAccessException();

    private static final ClassValue<Map<Method, Boolean>> SESSION_CHECK_CV = new ClassValue<>() {
        @Override
        protected Map<Method, Boolean> computeValue(@Nonnull Class<?> type) {
            return new ConcurrentHashMap<>();
        }
    };

    private final Function<Method, Boolean> singletonFinderLambda = m -> AnnotationUtils.findAnnotation(m, RequiredSession.class) != null;

    @EventListener(ApplicationReadyEvent.class)
    public void warm() {
        GlobalWarmUpManager.executor.execute(()->{
            AtomicInteger count = new AtomicInteger();
            try {
                asmAnnotationScanner.scanMethodAnnotation(RequiredSession.class).forEach((clazz, methods) -> {
                    Map<Method, Boolean> authMap = SESSION_CHECK_CV.get(clazz);
                    for (Method method : methods) {
                        authMap.put(method, Boolean.TRUE);
                        count.getAndIncrement();
                    }
                });
            } catch (Exception e) {
                log.warn("Failed to warm up RequiredSession", e);
            }
            log.info("{} @RequiredSession method(s) scanned.", count.get());
        });
    }

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) {
        if (!(handler instanceof HandlerMethod hm)) return true;

        Map<Method, Boolean> authMap = SESSION_CHECK_CV.get(hm.getBeanType());

        Boolean required = authMap.get(hm.getMethod());

        if (required == null) {
            required = authMap.computeIfAbsent(hm.getMethod(), singletonFinderLambda);
        }

        if (required == Boolean.TRUE && (request.getSession(false) == null)) {
            throw $ACCESS_DENIED;
        }

        return true;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this).addPathPatterns("/**").order(0);
    }
}