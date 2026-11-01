package com.demo.base.module.user.service;

import com.demo.infra.core.context.SessionContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final SessionContext sessionContext;


    public boolean isAuthed(String role) {
        return sessionContext.hasSession();
    }
}
