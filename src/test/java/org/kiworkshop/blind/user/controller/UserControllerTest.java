package org.kiworkshop.blind.user.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@SpringBootTest
@ContextConfiguration
class UserControllerTest {

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
}