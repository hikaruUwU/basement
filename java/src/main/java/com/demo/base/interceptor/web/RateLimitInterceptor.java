package com.demo.base.interceptor.web;

import com.demo.base.annotation.rateLimit.RateLimit;
import com.demo.base.config.GlobalWarmUpManager;
import com.demo.base.interceptor.WebInterceptorOrder;
import com.demo.base.spi.rate.RateLimiterStrategy;
import com.demo.base.exception.LimitationOverLoadException;
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
public class RateLimitInterceptor implements HandlerInterceptor, WebMvcConfigurer {
    private final ApplicationContext applicationContext;

    private final RateLimiterStrategy rateLimiterStrategy;

    private final LimitationOverLoadException $overload = new LimitationOverLoadException();

    private static final ClassValue<Map<Method, RateLimit>> RATE_LIMIT_CV = new ClassValue<>() {
        @Override
        protected Map<Method, RateLimit> computeValue(@Nonnull Class<?> type) {
            return new ConcurrentHashMap<>();
        }
    };

    private final Function<Method, RateLimit> singletonFinderLambda = m -> AnnotationUtils.findAnnotation(m, RateLimit.class);

    @EventListener(ApplicationReadyEvent.class)
    public void warmRateLimit() {
        GlobalWarmUpManager.executor.execute(() -> {
            long start = System.currentTimeMillis();
            AtomicInteger count = new AtomicInteger();
            try {
                Map<RequestMappingInfo, HandlerMethod> handlerMethods = applicationContext.getBean("requestMappingHandlerMapping",RequestMappingHandlerMapping.class).getHandlerMethods();

                for (HandlerMethod hm : handlerMethods.values()) {
                    Class<?> clazz = hm.getBeanType();
                    Method method = hm.getMethod();

                    Map<Method, RateLimit> rateMap = RATE_LIMIT_CV.get(clazz);

                    RateLimit rateLimit = AnnotationUtils.findAnnotation(method, RateLimit.class);

                    if (rateLimit != null) {
                        rateMap.put(method, rateLimit);
                        count.incrementAndGet();
                    }
                }
            } catch (Exception e) {
                log.warn("Failed to warm up RateLimit", e);
            }
            log.info("{} @RateLimit method(s) warmed in {} ms.", count.get(), System.currentTimeMillis() - start);
        });
    }

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) {
        if (!(handler instanceof HandlerMethod hm))
            return true;

        Map<Method, RateLimit> rateMap = RATE_LIMIT_CV.get(hm.getBeanType());

        RateLimit rateLimit = rateMap.get(hm.getMethod());

        if (rateLimit == null) {
            rateLimit = rateMap.computeIfAbsent(hm.getMethod(), singletonFinderLambda);
        }

        // if-return-proceed inline
        if (rateLimit != null) {
            boolean allowed = rateLimiterStrategy.allow(rateLimit.key(), rateLimit);
            if (!allowed) {
                throw $overload;
            }
        }

        return true;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this).addPathPatterns("/**").order(WebInterceptorOrder.RATE_LIMIT.getOrder());
    }
}
