package com.demo.base.annotation.logger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Monitor {
    LogRange[] range();

    LogLevel level();

    boolean Timer();

    boolean Argument();

    boolean Result();
}

