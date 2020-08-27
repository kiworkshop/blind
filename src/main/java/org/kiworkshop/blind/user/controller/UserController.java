package org.kiworkshop.blind.user.controller;

import javax.validation.Valid;

import org.kiworkshop.blind.user.controller.dto.UserPageRequest;
import org.kiworkshop.blind.user.controller.dto.UserRequestDto;
import org.kiworkshop.blind.user.controller.dto.UserResponseDto;
import org.kiworkshop.blind.user.domain.Role.ROLES;
import org.kiworkshop.blind.user.domain.User;
import org.kiworkshop.blind.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Secured(ROLES.ADMIN)
    public Page<User> getUsers(UserPageRequest userPageRequest) {
        return userService.getUsers(userPageRequest.getPageable());
    }

    @PostMapping
    public ResponseEntity<Long> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        Long id = userService.createUser(userRequestDto);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserBy(@PathVariable Long id) {
        UserResponseDto user = userService.readUserBy(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id,
                                           @RequestBody UserRequestDto userRequestDto) {
        userService.updateById(id, userRequestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
