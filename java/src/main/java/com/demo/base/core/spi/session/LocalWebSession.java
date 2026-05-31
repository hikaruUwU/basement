package com.demo.base.core.spi.session;

import com.demo.base.core.context.SessionContext;
import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Fallback;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Fallback
@Component
public class LocalWebSession implements SessionContext {
    public final static String USER_SESSION_IDENTIFIER = "client$identifier";

    @Override
    public boolean hasSession() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        return attributes.getRequest().getSession(false) != null;
    }

    @Override
    public void grant(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        if (attributes.getRequest().getSession(false) != null)
            attributes.getRequest().getSession(false).invalidate();
        attributes.getRequest().getSession(true);
    }

    @Override
    public void grant(@Nonnull Object identifier) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        if (attributes.getRequest().getSession(false) != null)
            attributes.getRequest().getSession(false).invalidate();
        attributes.getRequest().getSession(true).setAttribute(USER_SESSION_IDENTIFIER, identifier);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getIdentifier(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        return (T) attributes.getRequest().getSession(false).getAttribute(USER_SESSION_IDENTIFIER);
    }

    @Override
    public void revoke() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        attributes.getRequest().getSession(false).invalidate();
    }


}
