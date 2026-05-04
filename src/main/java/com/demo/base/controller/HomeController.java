package com.demo.base.controller;

import com.demo.base.annotation.prevalidate.PreValidate;
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
    @PreValidate(value = "T(Math).random() > 0.5", message = "Unauthed")
    public String home(@PathVariable String name) {
        return messageSource.getMessage("i.hello", null, LocaleContextHolder.getLocale()) + "," + name;
    }
}
