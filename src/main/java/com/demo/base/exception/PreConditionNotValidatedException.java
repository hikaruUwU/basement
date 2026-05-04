package com.demo.base.exception;

import lombok.experimental.StandardException;

import java.io.Serial;

@StandardException
public class PreConditionNotValidatedException extends RootException {
    @Serial
    private static final long serialVersionUID = 3533579535567000456L;

    public PreConditionNotValidatedException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable initCause(Throwable cause) {
        throw new UnsupportedOperationException("Wrap this cause is not allowed");
    }
}
