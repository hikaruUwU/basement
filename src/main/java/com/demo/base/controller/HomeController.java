package com.demo.base.controller;

import com.demo.base.annotation.prevalidate.PreValidate;
import com.demo.base.annotation.rateLimit.RateLimit;
import com.demo.base.annotation.requireSession.RequiredSession;
import com.demo.base.domain.User;
import com.demo.base.interceptor.SessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HomeController {

    @PostMapping("/login")
    @RateLimit(permit = 1, await = 1000)
    public void login(@RequestBody @Validated User user) {
        SessionManager.authorize(user);
    }

    @PostMapping("/info")
    @RequiredSession
    @PreValidate(value = "@sessionManager.identifier != null", message = "Login Expired")
    public User home() {
        return SessionManager.getIdentifier();
    }

    @GetMapping("/logout")
    @RequiredSession
    public void logout() {
        SessionManager.logoff();
    }
}
