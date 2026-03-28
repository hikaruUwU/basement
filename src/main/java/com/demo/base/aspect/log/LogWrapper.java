package com.demo.base.aspect.log;

import com.demo.base.annotation.logger.LogRange;
import com.demo.base.annotation.logger.Monitor;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Aspect
@Component
@RequiredArgsConstructor
public class LogWrapper {
    private final ObjectMapper objectMapper;

    private final ClassValue<Map<Method, LogExecutor>> CACHE = new ClassValue<>() {
        @Override
        protected Map<Method, LogExecutor> computeValue(@Nonnull Class<?> type) {
            return new ConcurrentHashMap<>();
        }
    };

    @FunctionalInterface
    private interface LogExecutor {
        Object execute(ProceedingJoinPoint jp) throws Throwable;
    }

    @Around("@annotation(com.demo.base.annotation.logger.Monitor)")
    public Object dynamicLogAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        LogExecutor executor = CACHE.get(method.getDeclaringClass()).computeIfAbsent(method, this::buildExecutor);

        return executor.execute(joinPoint);
    }

    private LogExecutor buildExecutor(Method m) {
        Monitor ann = AnnotationUtils.findAnnotation(m, Monitor.class);
        if (ann == null) return ProceedingJoinPoint::proceed;

        Level level = Level.toLevel(ann.level().name());
        if (!log.isEnabled(level)) return ProceedingJoinPoint::proceed;

        String prefix = m.getDeclaringClass().getSimpleName() + "." + m.getName() + "() >>>";
        int mask = calculateMask(ann.range());
        boolean hasTimer = ann.Timer();
        boolean hasArgs = ann.Argument();
        boolean hasResult = ann.Result();

        String callerInfo = m.getDeclaringClass().getSimpleName() + "." + m.getName();

        return jp -> {
            long start = hasTimer ? System.currentTimeMillis() : 0;

            if ((mask & (1 << LogRange.PRE.ordinal())) != 0) {
                log.atLevel(level).log("{} #PRE Args: {}", prefix, hasArgs ? safeSerialize(jp.getArgs()) : "[]");
            }

            Object result;
            try {
                result = jp.proceed();
            } catch (Throwable e) {

                if ((mask & (1 << LogRange.ERROR.ordinal())) != 0 || (mask & (1 << LogRange.POST.ordinal())) != 0) {
                    long cost = hasTimer ? System.currentTimeMillis() - start : 0;
                    log.atLevel(level).log("{} #Error [{}] {} Cost: {}ms", prefix, callerInfo, e.getMessage(), cost);
                }
                throw e;
            }

            boolean shouldLogSuccess = (mask & (1 << LogRange.SUCCESS.ordinal())) != 0;
            boolean shouldLogPost = (mask & (1 << LogRange.POST.ordinal())) != 0;

            if (shouldLogSuccess || shouldLogPost) {
                long cost = hasTimer ? System.currentTimeMillis() - start : 0;
                log.atLevel(level).log("{} #SUCCESS Return: {} Cost: {}ms{}", prefix, hasResult ? safeSerialize(new Object[]{result}) : "N/A", cost, shouldLogPost ? " #POST" : "");
            }

            return result;
        };
    }

    private @Nonnull Object safeSerialize(Object[] args) {
        if (args == null || args.length == 0) return "[]";

        return new Object() {
            @Override
            public String toString() {
                try {
                    return objectMapper.writeValueAsString(args.length == 1 ? args[0] : args);
                } catch (Exception e) {
                    return "[Serialization Error]";
                }
            }
        };
    }

    private int calculateMask(LogRange[] ranges) {
        int mask = 0;
        for (LogRange r : ranges) mask |= (1 << r.ordinal());
        return mask;
    }
}