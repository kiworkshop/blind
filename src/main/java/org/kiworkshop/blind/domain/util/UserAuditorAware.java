package org.kiworkshop.blind.domain.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kiworkshop.blind.security.LoginUserHolder;
import org.kiworkshop.blind.user.domain.User;
import org.kiworkshop.blind.user.repository.UserRepository;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserAuditorAware implements AuditorAware<User> {
    private final LoginUserHolder loginUserHolder;
    private final UserRepository userRepository;

    @Override
    public Optional<User> getCurrentAuditor() {
        return userRepository.findByEmail(loginUserHolder.get().getUsername());
    }
}
