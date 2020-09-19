package org.kiworkshop.blind.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class LoginUserHolder {
    public UserDetails get() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return (UserDetails) authentication.getPrincipal();
        } catch (Exception e) {
            throw new UnAuthenticationException(e);
        }
    }
}
