package com.demo.base.interceptor.web;

import com.demo.base.annotation.requireSession.RequiredSession;
import com.demo.base.config.GlobalWarmUpManager;
import com.demo.base.exception.UnauthenticatedAccessException;
import com.demo.base.interceptor.WebInterceptorOrder;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@Log4j2
@Configuration
@RequiredArgsConstructor
public class PassInterceptor implements HandlerInterceptor, WebMvcConfigurer {
    private final ApplicationContext applicationContext;
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
        GlobalWarmUpManager.executor.execute(() -> {
            final long start = System.currentTimeMillis();
            AtomicInteger count = new AtomicInteger();
            try {
                Map<RequestMappingInfo, HandlerMethod> handlerMethods = applicationContext.getBean("requestMappingHandlerMapping",RequestMappingHandlerMapping.class).getHandlerMethods();
                for (HandlerMethod hm : handlerMethods.values()) {
                    Class<?> clazz = hm.getBeanType();
                    Method method = hm.getMethod();
                    Map<Method, Boolean> authMap = SESSION_CHECK_CV.get(clazz);
                    boolean required = AnnotationUtils.findAnnotation(method, RequiredSession.class) != null;

                    if (required) {
                        authMap.put(method, Boolean.TRUE);
                        count.incrementAndGet();
                    }
                }
            } catch (Exception e) {
                log.warn("Failed to warm up RequiredSession", e);
            }
            log.info("{} @RequiredSession method(s) warmed in {} ms.", count.get(), System.currentTimeMillis() - start);
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
        registry.addInterceptor(this).addPathPatterns("/**").order(WebInterceptorOrder.PASS.getOrder());
    }
}