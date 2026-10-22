package com.demo.infra.core.aspect;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 切面优先级的唯一登记处 —— 数值越小越先执行。各切面在 {@code getOrder()}
 * 中返回这里的常量,相对次序集中在一处维护。
 * <p>
 * Single registry of aspect precedence — lower values run first. Aspects return these constants
 * from {@code getOrder()} so their relative ordering is maintained in one place.
 */
@AllArgsConstructor
public enum AspectOrder {
    PRE_VALIDATOR(0),
    AUDITOR(1);

    @Getter
    private final int order;
}