package com.demo.base.exception;

import lombok.experimental.StandardException;

import java.io.Serial;

@StandardException
public final class LimitationOverLoadException extends RootException {
    @Serial
    private static final long serialVersionUID = 4252814178348178760L;

    public LimitationOverLoadException() {
        super("Too Many Request, please retry.");
    }
}
