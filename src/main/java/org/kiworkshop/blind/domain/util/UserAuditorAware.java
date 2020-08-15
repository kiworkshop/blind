package org.kiworkshop.blind.domain.util;

import org.kiworkshop.blind.user.domain.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Component
public class UserAuditorAware implements AuditorAware<User> {

    @Override
    public Optional<User> getCurrentAuditor() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

        User currentUser = (User) attr.getRequest().getSession().getAttribute("LOGIN_USER");

        if (currentUser != null) return Optional.of(currentUser);
        return Optional.empty();
    }
}
