package com.demo.infra.core.util;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 在方法调用上下文中求值 SpEL:参数按名寻址(依赖 {@code -parameters} 编译),
 * bean 以 {@code @name} 寻址,目标对象为 root。字符串重载内置按目标类分域的
 * AST 缓存(MIXED 编译模式),调用方无需自缓存;亦接受预解析的 {@link Expression}。
 * <p>
 * Evaluates SpEL in the context of a method invocation: parameters addressable by name
 * (compiled with {@code -parameters}), beans by {@code @name}, the target object as root.
 * The String overload carries a built-in AST cache scoped per target class (MIXED compiler
 * mode) — callers need not cache themselves; a pre-parsed {@link Expression} is also accepted.
 */
@Component
public class SpelEvaluator {
    private final ExpressionParser parser;
    private final ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();
    private final BeanFactoryResolver beanResolver;

    private final ClassValue<Map<String, Expression>> expressionCache = new ClassValue<>() {
        @Override
        protected Map<String, Expression> computeValue(@NonNull Class<?> type) {
            return new ConcurrentHashMap<>();
        }
    };

    @Autowired
    public SpelEvaluator(ApplicationContext applicationContext) {
        SpelParserConfiguration config = new SpelParserConfiguration(
                SpelCompilerMode.MIXED,
                this.getClass().getClassLoader()
        );
        this.parser = new SpelExpressionParser(config);
        this.beanResolver = new BeanFactoryResolver(applicationContext);
    }

    public Object evaluate(String el, Method method, Object[] args, Object target) {
        if (!StringUtils.hasText(el)) {
            return null;
        }

        Class<?> targetClass = (target != null) ? target.getClass() : method.getDeclaringClass();

        Expression expression = expressionCache.get(targetClass).computeIfAbsent(el, parser::parseExpression);

        return evaluate(expression, method, args, target);
    }

    @SuppressWarnings("unchecked")
    public <T> T evaluate(Expression el, Method method, Object[] args, Object target) {
        MethodBasedEvaluationContext context = new MethodBasedEvaluationContext(
                target,
                method,
                args,
                nameDiscoverer
        );
        context.setBeanResolver(beanResolver);

        return (T)el.getValue(context);
    }
}