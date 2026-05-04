package com.demo.base.annotation.prevalidate;

import org.intellij.lang.annotations.Language;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreValidate {
    @Language("SpEL")
    String value();

    String message() default "";
}
