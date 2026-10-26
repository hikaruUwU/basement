package com.demo.infra.core.aspect.preValidate;

import com.demo.infra.core.annotation.prevalidate.PreValidate;
import com.demo.infra.core.aspect.AspectOrder;
import com.demo.infra.core.util.SpelEvaluator;
import com.demo.infra.exception.PreConditionNotValidatedException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Aspect
@Component
@RequiredArgsConstructor
public class PreValidator implements Ordered {
    private final SpelEvaluator spelEvaluator;
    private final MessageSource messageSource;

    @Override
    public int getOrder() {
        return AspectOrder.PRE_VALIDATOR.getOrder();
    }

    @Around("@annotation(com.demo.infra.core.annotation.prevalidate.PreValidate)")
    public Object validate(ProceedingJoinPoint joinPoint) throws Throwable {
        var method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        for (PreValidate ann : method.getAnnotationsByType(PreValidate.class)) {
            Object result = spelEvaluator.evaluate(
                    ann.value(),
                    method,
                    joinPoint.getArgs(),
                    joinPoint.getTarget()
            );

            if (!Boolean.TRUE.equals(result)) {
                throw new PreConditionNotValidatedException(
                        StringUtils.hasText(ann.message()) ? messageSource.getMessage(ann.message(), null, LocaleContextHolder.getLocale()) : null
                );
            }
        }

        return joinPoint.proceed();
    }
}
