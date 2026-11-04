package com.demo.infra.core.context;

import com.demo.infra.core.spi.context.LocalWebSession;
import jakarta.annotation.Nonnull;

/**
 * 会话机制的 SPI:存在性检查、授予(可选绑定身份标识;旧会话一律失效)、
 * 身份标识读取、撤销。
 * <p>
 * 默认绑定为 servlet 会话实现的 {@link LocalWebSession}
 * ({@code @Fallback});部署方可提供自己的 bean 替换实现。
 * 访问控制({@code PassInterceptor})与审计盖章({@code DefaultAuditFieldManager})均构建于本接口之上。
 * <p>
 * SPI over the session mechanism: existence check, grant (optionally binding an identifier; any
 * previous session is invalidated), identifier retrieval, and revocation.
 * <p>
 * The default binding is the servlet-session-backed
 * {@link LocalWebSession} ({@code @Fallback}); a deployment may
 * substitute another implementation by contributing its own bean. Access enforcement
 * ({@code PassInterceptor}) and audit stamping ({@code DefaultAuditFieldManager}) both build on this
 * interface.
 */
public interface SessionContext {
    boolean hasSession();

    void grant();

    void grant(@Nonnull Object identifier);

    <T> T getIdentifier();

    void revoke();

    void put(String key, Object value);

    <T> T get(String key);
}