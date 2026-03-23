package com.demo.base.config;

import com.demo.base.domain.response.Result;
import com.demo.base.exception.RootException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@Log4j2
@RestControllerAdvice
public class GlobalErrorWebExceptionHandler {
    private static final ProblemDetail $401 = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Access Denied");

    @ExceptionHandler(RootException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleRootException(RootException ex) {
        return Result.fail(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleValidationException(MethodArgumentNotValidException ex) {
        return Result.fail(Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ProblemDetail handleAccessDeniedException(AccessDeniedException ignored) {
        return $401;
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleGeneralException(Exception ex) {
        log.fatal(ex);
        return Result.fail(ex.getMessage());
    }
}