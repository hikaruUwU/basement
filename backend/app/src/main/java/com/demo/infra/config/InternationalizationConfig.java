package com.demo.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.Locale;

@Configuration
@SuppressWarnings("unused")
public class InternationalizationConfig {
    public static final String PAYLOAD_INCOMPLETE_OR_FORMAT_INCORRECT = "universal.input.properties.missing.or.formatIncorrect";

    public static final String FAILED = "universal.failed";

    public static final String UNEXISTS = "universal.not.exists";

    public static final String SUCCESS = "universal.success";

    public static final String LOGIN_EXPIRED = "universal.login.expired";

    public static final String ACCESS_UNAUTHORIZED = "universal.access.unauthorized";

    public static final String INTERNAL_ERROR = "universal.internal.error";

    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        localeResolver.setSupportedLocales(
                Arrays.asList(
                        Locale.SIMPLIFIED_CHINESE,
                        Locale.US
                ));
        return localeResolver;
    }
}
