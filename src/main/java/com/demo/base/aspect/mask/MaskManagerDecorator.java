package com.demo.base.aspect.mask;

import com.demo.base.annotation.mask.Sensitive;
import com.demo.base.util.FastAccess;
import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.invoke.VarHandle;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.util.ReflectionUtils.doWithFields;

@Log4j2
@Aspect
@Component
@Order(3)
public class MaskManagerDecorator {
    @FunctionalInterface
    private interface MaskProcessor {
        void process(Object target);
    }

    private static final MaskProcessor NO_OP = target -> {
    };

    private record MaskingPlan(VarHandle varHandle, String maskType, boolean isPenetrate) {
    }

    private final ClassValue<MaskProcessor> PROCESSOR_CACHE = new ClassValue<>() {
        @Override
        protected MaskProcessor computeValue(@Nonnull Class<?> type) {
            return createProcessor(type);
        }
    };

    public static final String PENETRATE = "penetrate";

    @PostConstruct
    public void hijack() {
        SensitiveFieldAlterer.registerMaskProcessor(PENETRATE, this::handleObject);
    }

    @Around("@annotation(com.demo.base.annotation.mask.MaskReturn)")
    public Object aroundMask(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        return handleObject(result);
    }

    private Object handleObject(Object o) {
        if (o == null) {
            return null;
        }

        PROCESSOR_CACHE.get(o.getClass()).process(o);
        return o;
    }

    private MaskProcessor createProcessor(Class<?> clazz) {
        if (clazz.isPrimitive() || isWrapperType(clazz) || clazz.getName().startsWith("java.lang")) {
            return NO_OP;
        }

        if (Collection.class.isAssignableFrom(clazz)) {
            return target -> {
                for (Object item : (Collection<?>) target) {
                    handleObject(item);
                }
            };
        }

        if (clazz.isArray()) {
            return target -> {
                int length = Array.getLength(target);
                for (int i = 0; i < length; i++) {
                    handleObject(Array.get(target, i));
                }
            };
        }

        List<MaskingPlan> plans = buildPlans(clazz);
        if (plans.isEmpty()) {
            return NO_OP;
        }

        return target -> {
            for (MaskingPlan plan : plans) {
                Object val = plan.varHandle.get(target);
                if (val == null) continue;

                if (plan.isPenetrate) {
                    handleObject(val);
                } else {
                    Object masked = SensitiveFieldAlterer.mask(plan.maskType, val);
                    plan.varHandle.set(target, masked);
                }
            }
        };
    }

    private List<MaskingPlan> buildPlans(Class<?> clazz) {
        List<MaskingPlan> plans = new ArrayList<>();

        doWithFields(clazz, field -> {
            Sensitive anno = field.getAnnotation(Sensitive.class);
            if (anno != null) {
                VarHandle vh = FastAccess.bootstrapVarHandle(clazz, field.getName());
                plans.add(new MaskingPlan(vh, anno.value(), PENETRATE.equals(anno.value())));
            }
        });
        return plans;
    }

    private boolean isWrapperType(Class<?> clazz) {
        return clazz == Integer.class || clazz == Long.class || clazz == Boolean.class || clazz == Double.class || clazz == Float.class || clazz == Short.class || clazz == Byte.class || clazz == Character.class;
    }
}