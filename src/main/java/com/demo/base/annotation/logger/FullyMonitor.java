package com.demo.base.annotation.logger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Monitor(
        range = {LogRange.POST},
        level = LogLevel.INFO,
        Timer = true,
        Result = true,
        Argument = true
)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FullyMonitor {
}
