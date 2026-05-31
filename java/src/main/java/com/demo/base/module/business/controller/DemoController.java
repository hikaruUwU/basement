package com.demo.base.module.business.controller;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
public class DemoController {
    @GetMapping("/demo")
    public String demo() {
        return LocalDateTime.now().toString();
    }
}
