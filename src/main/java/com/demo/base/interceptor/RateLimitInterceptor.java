package com.demo.base.interceptor;

import com.demo.base.annotation.RateLimit;
import com.demo.base.component.rate.RateLimiterStrategy;
import com.demo.base.exception.LimitationOverLoadException;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor, WebMvcConfigurer {
    private final RateLimiterStrategy rateLimiterStrategy;

    private final LimitationOverLoadException $overload = new LimitationOverLoadException();

    private static final ClassValue<Map<Method, RateLimit>> RATE_LIMIT_CV = new ClassValue<>() {
        @Override
        protected Map<Method, RateLimit> computeValue(@Nonnull Class<?> type) {
            return new ConcurrentHashMap<>();
        }
    };

    private final Function<Method, RateLimit> singletonFinderLambda = m -> AnnotationUtils.findAnnotation(m, RateLimit.class);

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
        // priority is higher than PassInterceptor (order 0)
        registry.addInterceptor(this).addPathPatterns("/**").order(-1);
    }
}
