package com.demo.base.aspect;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
public enum AspectOrder {
    PRE_VALIDATOR(0),
    LOG_WRAPPER(1);

    @Getter
    private final int order;
}