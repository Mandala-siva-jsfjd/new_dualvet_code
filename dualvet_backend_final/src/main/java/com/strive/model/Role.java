package com.strive.model;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public enum Role {
    USER,
    ADMIN;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        switch (this) {
            case USER:
                return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
            case ADMIN:
                return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
            default:
                return Collections.emptyList();
        }
    }
}

