package com.demo.base.config;

import com.demo.base.core.exception.RootException;
import com.demo.base.core.exception.UnauthenticatedAccessException;
import com.demo.base.shared.Result;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Objects;

@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalErrorWebExceptionHandler {
    private static final Result<Void> $401 = Result.fail("Access Denied");

    @ExceptionHandler(RootException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleRootException(RootException ex) {
        return Result.fail(ex.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleValidationException(MethodArgumentNotValidException ex) {
        return Result.fail(Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException ex) {
        return Result.fail(ex.getConstraintViolations().iterator().next().getMessage());
    }

    @ExceptionHandler(UnauthenticatedAccessException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<Void> handleAccessDeniedException(UnauthenticatedAccessException ignored) {
        return $401;
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, NoResourceFoundException.class})
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleHttpMessageNotReadableException(Exception ex) {
        return Result.fail(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleGeneralException(Exception ex) {
        log.fatal(ex);
        return Result.fail(ex.getMessage());
    }
}