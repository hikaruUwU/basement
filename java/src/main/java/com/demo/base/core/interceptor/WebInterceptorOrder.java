package com.demo.base.core.interceptor;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum WebInterceptorOrder {
    PASS(1);

    @Getter
    private final int order;
}