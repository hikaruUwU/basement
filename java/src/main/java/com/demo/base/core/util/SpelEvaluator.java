package com.demo.base.core.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@RequiredArgsConstructor
public class SpelEvaluator {
    private final ExpressionParser parser = new SpelExpressionParser();
    private final ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    private final ApplicationContext applicationContext;

    public Object evaluate(String el, Method method, Object[] args, Object target) {

        Expression expression = parser.parseExpression(el);

        MethodBasedEvaluationContext context = new MethodBasedEvaluationContext(target, method, args, nameDiscoverer);

        context.setBeanResolver(new BeanFactoryResolver(applicationContext));
        context.setRootObject(target);

        return expression.getValue(context);
    }

    public Object evaluate(Expression el, Method method, Object[] args, Object target) {
        MethodBasedEvaluationContext context = new MethodBasedEvaluationContext(target, method, args, nameDiscoverer);

        context.setBeanResolver(new BeanFactoryResolver(applicationContext));
        context.setRootObject(target);

        return el.getValue(context);
    }

}
