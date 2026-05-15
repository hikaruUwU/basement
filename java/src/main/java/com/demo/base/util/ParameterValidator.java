package com.demo.base.util;

import com.demo.base.exception.RootException;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@UtilityClass
@SuppressWarnings("unused")
public class ParameterValidator {

    public static class FailFastValidator<T> {
        private final T target;

        private FailFastValidator(T target) {
            this.target = target;
        }

        public static <T> FailFastValidator<T> of(T target) {
            return new FailFastValidator<>(target);
        }

        public FailFastValidator<T> ensure(Function<T, Boolean> predicate, String failMessage) {
            if (Boolean.TRUE.equals(predicate.apply(target))) {
                throw new RootException(failMessage);
            }
            return this;
        }
    }

    public static class AccumulatingValidator<T> {
        private final T target;
        private final List<String> errors = new ArrayList<>();

        private AccumulatingValidator(T target) {
            this.target = target;
        }

        public static <T> AccumulatingValidator<T> of(T target) {
            return new AccumulatingValidator<>(target);
        }

        public AccumulatingValidator<T> addRule(Function<T, Boolean> predicate, String failMessage) {
            if (Boolean.TRUE.equals(predicate.apply(target))) {
                errors.add(failMessage);
            }
            return this;
        }

        public void validate() {
            if (!errors.isEmpty()) {
                throw new RootException(String.join(", ", errors));
            }
        }

        public List<String> validateWithoutThrowing() {
            return Collections.unmodifiableList(errors);
        }
    }
}