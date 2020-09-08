package org.kiworkshop.blind.domain.util;

import lombok.extern.slf4j.Slf4j;
import org.kiworkshop.blind.security.LoginUserHolder;
import org.kiworkshop.blind.user.domain.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class UserAuditorAware implements AuditorAware<User> {
    @Override
    public Optional<User> getCurrentAuditor() {
        return Optional.of(LoginUserHolder.get());
    }
}
