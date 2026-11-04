package com.demo.infra.config;

import com.demo.infra.exception.RootException;
import com.demo.infra.exception.UnauthenticatedAccessException;
import com.demo.infra.data2object.Result;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@AllArgsConstructor
public class GlobalErrorWebExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler(RootException.class)
    @ResponseStatus(HttpStatus.OK)
    public @Nonnull Map<String, String> handleRootException(@Nonnull final RootException ex) {
        return Map.of(
                Result.Fields.message,
                ex.getMessage()
        );
    }

    @ExceptionHandler(UnauthenticatedAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public @Nonnull Map<String, Serializable> handleAccessDeniedException(@Nonnull final UnauthenticatedAccessException ignored) {
        return Map.of(
                Result.Fields.message, messageSource.getMessage(
                        InternationalizationConfig.LOGIN_EXPIRED,
                        null,
                        LocaleContextHolder.getLocale()
                ),
                Result.Fields.code, 401
        );
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            NoResourceFoundException.class,
            MissingRequestCookieException.class,
            IllegalArgumentException.class
    })
    @ResponseStatus(HttpStatus.OK)
    public @Nonnull Map<String, String> handleHttpMessageNotReadableException(@Nonnull final Exception ex) {
        return Map.of(
                Result.Fields.message,
                ex.getMessage()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @Nonnull Map<String, String> handleGeneralException(@Nonnull final Exception ex) {
        final var ref = ThreadLocalRandom.current().nextLong();

        log.fatal(ref, ex);

        return Map.of(
                Result.Fields.message,
                messageSource.getMessage(
                        InternationalizationConfig.INTERNAL_ERROR,
                        new Object[]{ref},
                        LocaleContextHolder.getLocale()
                )
        );
    }
}
