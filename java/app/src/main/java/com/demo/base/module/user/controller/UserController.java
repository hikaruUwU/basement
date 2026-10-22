package com.demo.base.module.user.controller;

import com.demo.infra.config.InternationalizationConfig;
import com.demo.infra.core.annotation.prevalidate.PreValidate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


@RestController
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/")
    @PreValidate(value = "@userController.random()", message = InternationalizationConfig.FAILED)
    public Object hello() {
        return this.getClass();
    }

    public boolean random(){
        return ThreadLocalRandom.current().nextBoolean();
    }

}