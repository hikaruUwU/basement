package com.demo.infra.core.util;


import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class DataValidator {
    private final Map<Rule, Object> rules = new HashMap<>();

    private enum Rule {
        NOT_NULL,
        NOT_EMPTY,
        HAS_TEXT,
        IS_EMAIL,
        MAX_THAN,
        CUSTOMIZE
    }

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private DataValidator() {
    }

    public static DataValidator chaining() {
        return new DataValidator();
    }

    private DataValidator notNullable(@Nullable Object value) {
        rules.put(Rule.NOT_NULL, value);
        return this;
    }

    private DataValidator notEmpty(@Nonnull Collection<?> value) {
        rules.put(Rule.NOT_EMPTY, value);
        return this;
    }

    private DataValidator hasText(@Nullable String value) {
        rules.put(Rule.HAS_TEXT, value);
        return this;
    }

    private DataValidator isEmail(@Nullable String value) {
        rules.put(Rule.IS_EMAIL, value);
        return this;
    }

    private <T> DataValidator assertThat(T value, Predicate<T> rule){
        rules.put(Rule.CUSTOMIZE, new Object[]{value, rule});
        return this;
    }

    @SuppressWarnings("unchecked")
    private boolean validate() {
        for (Map.Entry<Rule, Object> entry : rules.entrySet()) {

            switch (entry.getKey()) {
                case NOT_NULL:
                    if (entry.getValue() == null) return false;
                    break;
                case NOT_EMPTY:
                    if (((Collection<?>) entry.getValue()).isEmpty()) return false;
                    break;
                case HAS_TEXT:
                    if (entry.getValue() == null || ((String) entry.getValue()).isBlank()) return false;
                    break;
                case IS_EMAIL:
                    if (entry.getValue() == null || !EMAIL_PATTERN.matcher((String) entry.getValue()).matches()) return false;
                    break;
                case CUSTOMIZE:
                    Object[] customize = (Object[]) entry.getValue();
                    if (!((Predicate<Object>) customize[1]).test(customize[0])) return false;
                    break;
            }
        }

        return true;
    }


}
