package org.kiworkshop.blind.user.repository;

import java.util.Optional;

import org.kiworkshop.blind.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
