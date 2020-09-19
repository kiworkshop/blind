package org.kiworkshop.blind.post.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class PostControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    private String A;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }

    private static List<Arguments> postMappings() {
        return Collections.emptyList();
    }

    @MethodSource(value = "postMappings")
    @ParameterizedTest
    void unAuthenticated(String url, MultiValueMap<String, String> params) throws Exception {
        MvcResult mvcResult = mvc.perform(
            get("/posts")
                .param("page", "0")
                .param("size", "10")
        )
            .andExpect(
                status().is4xxClientError()
            ).andReturn();
    }

    @MethodSource(value = "postMappings")
    @ParameterizedTest
    void authenticated(String url, MultiValueMap<String, String> params) throws Exception {
        MvcResult mvcResult = mvc.perform(
            get(url)
                .params(params)
        )
            .andExpect(
                status().is2xxSuccessful()
            ).andReturn();
    }

    @Test
    @WithMockUser
    void getAllAuthenticated() throws Exception {
        MvcResult mvcResult = mvc.perform(
            get("/posts")
                .param("page", "0")
                .param("size", "10")
        )
            .andExpect(
                status().is2xxSuccessful()
            ).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
    }

    @Test
    void createPost() {
    }

    @Test
    void likePost() {
    }

    @Test
    void getPostById() {
    }

    @Test
    void updatePost() {
    }

    @Test
    void deletePost() {
    }

    @Test
    void addComment() {
    }

    @Test
    void getAllComments() {
    }

    @Test
    void getPartialComments() {
    }

    @Test
    void getRestComments() {
    }
}
