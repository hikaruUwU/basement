package com.demo.base.aspect.preValidate;

import com.demo.base.annotation.prevalidate.PreValidate;
import com.demo.base.config.GlobalWarmUpManager;
import com.demo.base.util.ASMAnnotationScanner;
import com.demo.base.util.SpelEvaluator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ApplicationContext;
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
public class PreValidator {
    private final SpelEvaluator spelEvaluator;
    private final ApplicationContext applicationContext;

    private record ValidateCacheEntry(byte hasExpression, Expression expression) {}

    private static final ExpressionParser PARSER = new SpelExpressionParser(
            new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE, PreValidator.class.getClassLoader())
    );

    private static final RuntimeException $ACCESS_DENIED = new RuntimeException("Access Denied");

    private static final Function<Method, ValidateCacheEntry> MAPPING_FUNCTION = method -> {
        PreValidate ann = AnnotationUtils.findAnnotation(method, PreValidate.class);
        if (ann != null && !ann.value().isEmpty()) {
            return new ValidateCacheEntry((byte) 1, PARSER.parseExpression(ann.value()));
        }
        return new ValidateCacheEntry((byte) 0, null);
    };

    private static final ClassValue<Map<Method, ValidateCacheEntry>> CLASS_CACHE = new ClassValue<>() {
        @Override
        protected Map<Method, ValidateCacheEntry> computeValue(@NotNull Class<?> type) {
            return new ConcurrentHashMap<>();
        }
    };

    @PostConstruct
    public void warmUp() {
        GlobalWarmUpManager.executor.execute(()->{
            try {
                String basePackage = AutoConfigurationPackages.get(applicationContext).getFirst();
                Map<Class<?>, List<Method>> annotatedData = ASMAnnotationScanner.scanMethodAnnotation(basePackage, PreValidate.class);

                annotatedData.forEach((clazz, methods) -> {
                    Map<Method, ValidateCacheEntry> methodMap = CLASS_CACHE.get(clazz);
                    for (Method method : methods) {
                        methodMap.put(method, MAPPING_FUNCTION.apply(method));
                    }

                });
                log.info("PreValidate warm-up complete with {} scanned.", annotatedData.size());
            } catch (Exception e) {
                log.error("PreValidate warm-up failed", e);
            }
        });
    }

    @Around("@annotation(com.demo.base.annotation.prevalidate.PreValidate)")
    public Object validate(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Map<Method, ValidateCacheEntry> methodMap = CLASS_CACHE.get(method.getDeclaringClass());

        // 1. 直接尝试 get (最快路径，无闭包，无锁)
        ValidateCacheEntry entry = methodMap.get(method);

        // 2. 如果未命中，使用静态常量 Function 进行计算
        if (entry == null) {
            entry = methodMap.computeIfAbsent(method, MAPPING_FUNCTION);
        }

        if (entry.hasExpression() == 1) {
            Object result = spelEvaluator.evaluate(
                    entry.expression(),
                    method,
                    joinPoint.getArgs(),
                    joinPoint.getTarget()
            );

            if (Boolean.FALSE.equals(result)) {
                throw $ACCESS_DENIED;
            }
        }

        return joinPoint.proceed();
    }
}