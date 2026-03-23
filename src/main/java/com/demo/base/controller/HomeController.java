package com.demo.base.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HomeController {
    private final MessageSource messageSource;

    @GetMapping("/hello/{name}")
    public String home(@PathVariable String name) {
        return messageSource.getMessage("i.hello", null, LocaleContextHolder.getLocale()) + "," + name;
    }
}
