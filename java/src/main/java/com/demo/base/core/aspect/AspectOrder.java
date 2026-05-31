package com.demo.base.core.aspect;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
public enum AspectOrder {
    PRE_VALIDATOR(0);

    @Getter
    private final int order;
}