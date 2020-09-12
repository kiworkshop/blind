package org.kiworkshop.blind.user.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Role {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    public static class ROLES {
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }

    private String authority;

    public String getAuthority() {
        return authority;
    }
}
