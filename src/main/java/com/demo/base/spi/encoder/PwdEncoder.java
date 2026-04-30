package com.demo.base.spi.encoder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Fallback;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PwdEncoder {
    @Bean
    @Fallback
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder(16, 32, 1, 8192, 2);
    }
}