package com.demo.base.core.context;

import jakarta.annotation.Nonnull;

public interface SessionContext {
    boolean hasSession();

    void grant();

    void grant(@Nonnull Object identifier);

    <T> T getIdentifier();

    void revoke();
}