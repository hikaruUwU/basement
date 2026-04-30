package com.demo.base.exception;

import lombok.experimental.StandardException;

import java.io.Serial;

@StandardException
public class UnauthenticatedAccessException extends RootException {
    @Serial
    private static final long serialVersionUID = -3996210483394118456L;

    public UnauthenticatedAccessException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable initCause(Throwable cause) {
        throw new UnsupportedOperationException("Cannot wrap this cause");
    }
}
