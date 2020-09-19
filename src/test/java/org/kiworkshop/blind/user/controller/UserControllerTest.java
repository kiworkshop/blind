package org.kiworkshop.blind.user.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kiworkshop.blind.user.controller.dto.UserRequestDto;
import org.kiworkshop.blind.user.repository.UserRepository;
import org.kiworkshop.blind.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@SpringBootTest
class UserControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("User가 admin일때 user정보를 가져올수있다")
    public void getUsers_admin() throws Exception {
        mvc.perform(get("/users"))
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    @DisplayName("User가 admin아닐때 user정보를 가져올수없다")
    public void getUsers_default_user() throws Exception {
        mvc.perform(get("/users"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("User가 admin일때 다른 user들의 정보도 가져올수있다")
    public void getUserBy_admin() throws Exception {
        userService.createUser(UserRequestDto.builder().email("admin").password("1234").build());
        userService.createUser(UserRequestDto.builder().email("harris").password("1234").build());
        mvc.perform(get("/users/harris"))
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    @DisplayName("User가 admin아닐때 다른 user의 정보를 가져올수없다")
    public void getUsersBy_default_user() throws Exception {
        userService.createUser(UserRequestDto.builder().email("admin").password("1234").build());
        userService.createUser(UserRequestDto.builder().email("harris").password("1234").build());
        mvc.perform(get("/users/admin"))
            .andExpect(status().isForbidden());
    }
}
