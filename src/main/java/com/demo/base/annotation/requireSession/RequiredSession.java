package com.demo.base.annotation.requireSession;

import org.intellij.lang.annotations.Language;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiredSession {
    @Language("SpEL")
    String value() default "";
}