package org.kiworkshop.blind.user.service;

import org.kiworkshop.blind.user.controller.dto.UserRequestDto;
import org.kiworkshop.blind.user.controller.dto.UserResponseDto;
import org.kiworkshop.blind.user.domain.User;
import org.kiworkshop.blind.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Transactional
    public Long createUser(UserRequestDto userRequestDto) {
        User user = userRequestDto.toEntity();
        return userRepository.save(user).getId();
    }

    public UserResponseDto readUserBy(String username) {
        User user = userRepository.findByEmail(username)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 id입니다."));
        return UserResponseDto.from(user);
    }

    public void updateById(Long id, UserRequestDto userRequestDto) {
        User userToUpdate = userRequestDto.toEntity();
        User user = findById(id);
        user.update(userToUpdate);
        userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 id입니다."));
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 email 입니다."));
    }
}
