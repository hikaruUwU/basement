package com.demo.base.exception;

import lombok.experimental.StandardException;

import java.io.Serial;

@StandardException
public final class NoConditionFoundedException extends RootException {
    @Serial
    private static final long serialVersionUID = 5881724160782893196L;

    public NoConditionFoundedException() {
        super("Should At Least Providing One Condition.");
    }
}
