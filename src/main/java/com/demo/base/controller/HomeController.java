package com.demo.base.controller;

import com.demo.base.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController
@RequiredArgsConstructor
public class HomeController {

    @PostMapping("/test")
    public Serializable home(@RequestBody @Validated User user) {
        return user;
    }
}
