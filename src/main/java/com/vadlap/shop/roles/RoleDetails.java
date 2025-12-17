package com.vadlap.shop.roles;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

public interface RoleDetails extends Serializable {
    Collection<? extends GrantedAuthority> getAuthorities();

    Long getId();
    String getUsername();

    default boolean getIsAccountExpired() {
        return true;
    }

    default boolean getIsAccountLocked() {
        return true;
    }

    default boolean getIsCredentialsExpired() {
        return true;
    }

    default boolean getIsEnabled() {
        return true;
    }
}
