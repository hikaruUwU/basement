package com.demo.base.interceptor;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum WebInterceptorOrder {

    RATE_LIMIT(0),
    PASS(1);

    @Getter
    private final int order;
}