package com.demo.base.interceptor;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class SessionManager implements HttpSessionListener {
    public final static String USER_SESSION_IDENTIFIER = "client$identifier";

    public static boolean hasSession() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        return attributes.getRequest().getSession(false) != null;
    }

    public static void authorize(@Nonnull Object identifier) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        if (attributes.getRequest().getSession(false) != null)
            attributes.getRequest().getSession(false).invalidate();
        attributes.getRequest().getSession(true).setAttribute(USER_SESSION_IDENTIFIER, identifier);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getIdentifier(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        return (T) attributes.getRequest().getSession(false).getAttribute(USER_SESSION_IDENTIFIER);
    }

    public static void logoff() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        attributes.getRequest().getSession(false).invalidate();
    }

    public static void logoff(@Nonnull HttpSession session) {
        session.invalidate();
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {

    }
}
