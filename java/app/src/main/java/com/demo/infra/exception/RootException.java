package com.demo.infra.exception;

import lombok.experimental.StandardException;

import java.io.Serial;

/**
 * 业务异常体系的根:非受检且无栈({@link #fillInStackTrace()} 为空操作),热路径上抛出开销极低。
 * 由 {@code GlobalErrorWebExceptionHandler} 渲染为 HTTP 200 上的失败 {@code Result};
 * message 应为可直接呈现给用户的文案,通常已本地化。
 * <p>
 * Root of the business exception hierarchy: unchecked and stackless
 * ({@link #fillInStackTrace()} is a no-op), so throwing is cheap on hot paths. Rendered by
 * {@code GlobalErrorWebExceptionHandler} as a failed {@code Result} on an HTTP 200 response;
 * the message is expected to be user-presentable, typically already localized.
 */
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
