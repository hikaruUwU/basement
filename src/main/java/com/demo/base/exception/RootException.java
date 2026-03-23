package com.demo.base.exception;

import lombok.experimental.StandardException;

import java.io.Serial;

@StandardException
public class RootException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 6958766271475463456L;

    public RootException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
