package org.kiworkshop.blind.security;

import org.kiworkshop.blind.user.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class LoginUserHolder {
    public static User get() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return (User) authentication.getPrincipal();
        } catch (Exception e) {
            throw new UnAuthenticationException(e);
        }
    }
}
