package com.demo.base.util;

import jakarta.annotation.Nullable;

import java.util.function.BiFunction;

@SuppressWarnings("unused")
public class StateMachine<S extends Enum<S>, A extends Enum<A>, E> {
    private final S[][] matrix;

    public record Transition<S, A>(S from, A action, S to) {
    }

    @SuppressWarnings("unchecked")
    public StateMachine(Class<S> stateClass, Class<A> actionClass, Iterable<Transition<S, A>> transitions) {
        int stateCount = stateClass.getEnumConstants().length;
        int actionCount = actionClass.getEnumConstants().length;
        this.matrix = (S[][]) new Enum[stateCount][actionCount];
        transitions.forEach(t -> matrix[t.from().ordinal()][t.action().ordinal()] = t.to());
    }

    public boolean canTransfer(S from, A action, S to) {
        S next = matrix[from.ordinal()][action.ordinal()];
        return next == to;
    }

    public @Nullable S transfer(S from, A action) {
        return matrix[from.ordinal()][action.ordinal()];
    }

    public S transfer(S from, A action, BiFunction<S, A, S> ifCannot) {
        S transfer = transfer(from, action);
        return transfer != null ? transfer : ifCannot.apply(from, action);
    }

}