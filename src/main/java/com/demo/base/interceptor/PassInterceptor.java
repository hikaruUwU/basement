package com.demo.base.interceptor;

import com.demo.base.annotation.RequiredSession;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.Serial;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Configuration
public class PassInterceptor implements HandlerInterceptor, WebMvcConfigurer {
    private final AccessDeniedException $ACCESS_DENIED = new AccessDeniedException(null) {
        @Serial
        private static final long serialVersionUID = 8935745108846751860L;

        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }

        @Override
        public synchronized Throwable initCause(Throwable cause) {
            throw new UnsupportedOperationException("Cannot wrap any cause in this specified exception");
        }
    };

    private static final ClassValue<Map<Method, Boolean>> SESSION_CHECK_CV = new ClassValue<>() {
        @Override
        protected Map<Method, Boolean> computeValue(@Nonnull Class<?> type) {
            return new ConcurrentHashMap<>();
        }
    };

    private final Function<Method, Boolean> singletonFinderLambda = m -> AnnotationUtils.findAnnotation(m, RequiredSession.class) != null;

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
        registry.addInterceptor(this).addPathPatterns("/**");
    }
}