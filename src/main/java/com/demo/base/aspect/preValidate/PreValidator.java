package com.demo.base.aspect.preValidate;

import com.demo.base.annotation.prevalidate.PreValidate;
import com.demo.base.aspect.AspectOrder;
import com.demo.base.config.GlobalWarmUpManager;
import com.demo.base.exception.PreConditionNotValidatedException;
import com.demo.base.util.ASMAnnotationScanner;
import com.demo.base.util.SpelEvaluator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Log4j2
@Aspect
@Component
@RequiredArgsConstructor
public class PreValidator implements Ordered {
    private final SpelEvaluator spelEvaluator;
    private final ASMAnnotationScanner asmAnnotationScanner;
    @Override
    public int getOrder() {
        return AspectOrder.getOrder(this);
    }

    private record ValidateCacheEntry(byte hasExpression, Expression expression, String $message) {}

    private static final ExpressionParser PARSER = new SpelExpressionParser(
            new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE, PreValidator.class.getClassLoader())
    );

    private static final Function<Method, ValidateCacheEntry> MAPPING_FUNCTION = method -> {
        PreValidate ann = AnnotationUtils.findAnnotation(method, PreValidate.class);
        if (ann != null && !ann.value().isEmpty()) {
            return new ValidateCacheEntry((byte) 1, PARSER.parseExpression(ann.value()), ann.message());
        }
        return new ValidateCacheEntry((byte) 0, null, null);
    };

    private static final ClassValue<Map<Method, ValidateCacheEntry>> CLASS_CACHE = new ClassValue<>() {
        @Override
        protected Map<Method, ValidateCacheEntry> computeValue(@NotNull Class<?> type) {
            return new ConcurrentHashMap<>();
        }
    };

    @EventListener(ApplicationReadyEvent.class)
    public void warmUp() {
        GlobalWarmUpManager.executor.execute(()->{
            long start = System.currentTimeMillis();
            try {
                Map<Class<?>, List<Method>> annotatedData = asmAnnotationScanner.scanMethodAnnotation(PreValidate.class);

                annotatedData.forEach((clazz, methods) -> {
                    Map<Method, ValidateCacheEntry> methodMap = CLASS_CACHE.get(clazz);
                    for (Method method : methods) {
                        methodMap.put(method, MAPPING_FUNCTION.apply(method));
                    }

                });
                log.info("PreValidate warm-up complete in {} ms with {} class(es) scanned.", System.currentTimeMillis() - start, annotatedData.size());
            } catch (Exception e) {
                log.error("PreValidate warm-up failed", e);
            }
        });
    }

    @Around("@annotation(com.demo.base.annotation.prevalidate.PreValidate)")
    public Object validate(ProceedingJoinPoint joinPoint) throws Throwable {
        var method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        Map<Method, ValidateCacheEntry> methodMap = CLASS_CACHE.get(method.getDeclaringClass());

        ValidateCacheEntry entry = methodMap.get(method);

        if (entry == null) {
            entry = methodMap.computeIfAbsent(method, MAPPING_FUNCTION);
        }

        if (entry.hasExpression == 1) {
            Object result = spelEvaluator.evaluate(
                    entry.expression(),
                    method,
                    joinPoint.getArgs(),
                    joinPoint.getTarget()
            );

            if (Boolean.FALSE.equals(result)) {
                throw new PreConditionNotValidatedException(entry.$message);
            }
        }

        return joinPoint.proceed();
    }
}