package com.demo.base.aspect;

import com.demo.base.aspect.log.LogWrapper;
import com.demo.base.aspect.preValidate.PreValidator;

import java.util.Map;

public abstract class AspectOrder {
    private static final Map<Class<?>, Integer> order = Map.ofEntries(
            Map.entry(PreValidator.class, 0),
            Map.entry(LogWrapper.class, 1)
    );

    public static Integer getOrder(Object aspectInstance) {
        return order.get(aspectInstance instanceof Class<?> instanceClass ? instanceClass : aspectInstance.getClass());
    }
}