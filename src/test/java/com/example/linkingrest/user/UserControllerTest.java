package com.example.linkingrest.user;

import com.example.linkingrest.config.security.CustomUserDetailsService;
import com.example.linkingrest.config.security.JwtProvider;
import com.example.linkingrest.config.security.service.SecurityService;
import com.example.linkingrest.user.controller.CreateUserRequest;
import com.example.linkingrest.user.controller.UserController;
import com.example.linkingrest.user.domain.Role;
import com.example.linkingrest.user.domain.User;
import com.example.linkingrest.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest(controllers = UserController.class,
//        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
@WebMvcTest(controllers = UserController.class,
        includeFilters = {@ComponentScan.Filter(classes = EnableWebSecurity.class)})
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@Import({JwtProvider.class})
public class UserControllerTest {

    @MockBean
    private UserService userService;
    @MockBean
    private SecurityService securityService;
    @MockBean
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("회원 가입")
    @Test
    @WithAnonymousUser
    public void saveUser() throws Exception{
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .name("user1")
                .email("user1@email.com")
                .password("123")
                .role(Role.ROLE_MENTOR)
                .img(null)
                .build();
        User user = createUserRequest.toEntity();
        given(userService.join(any())).willReturn(user.getId());
        given(userService.findById(any())).willReturn(user);

        final ResultActions actions = mockMvc.perform(MockMvcRequestBuilders
                .post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequest)))
                .andExpect(status().isCreated()).andDo(print());


    }
    @Nested
    @DisplayName("회원 조회 테스트")
    @WithMockUser(username = "test",password = "123",roles = {"MENTOR"})
    class find {
        @DisplayName("모든 회원 조회")
        @Test
        public void findAll() throws Exception {
            User user1 = User.builder()
                    .name("user1")
                    .email("user1@email.com")
                    .password("123")
                    .role(Role.ROLE_MENTOR)
                    .img(null)
                    .build();
            User user2 = User.builder()
                    .email("user2@email.com")
                    .password("2222")
                    .name("user2")
                    .role(Role.ROLE_MENTOR)
                    .img(null)
                    .build();
            User[] users = {user1, user2};
            given(userService.findUsers()).willReturn(List.of(users));

            mockMvc.perform(MockMvcRequestBuilders
                    .get("/users")).andExpect(status().isOk()).andExpect(jsonPath("$.users[0].name").value("user1"))
                    .andExpect(jsonPath("$.users[1].name").value("user2")).andDo(print());


        }
        @DisplayName("회원 ID로 조회")
        @Test
        public void findUserById() throws Exception {
            User user = User.builder()
                    .name("user1")
                    .email("user1@email.com")
                    .password("123")
                    .role(Role.ROLE_MENTOR)
                    .img(null)
                    .build();
            given(userService.findById(any())).willReturn(user);
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/users/1")).andExpect(status().isOk()).andExpect(jsonPath("name").value("user1")).andDo(print());

        }
    }
    @DisplayName("회원 삭제")
    @WithMockUser(username = "test",password = "123",roles = {"MENTOR"})
    @Test
    public void deleteUser() throws Exception{
        User user = User.builder()
                .name("user1")
                .email("user1@email.com")
                .password("123")
                .role(Role.ROLE_MENTOR)
                .img(null)
                .build();
        given(userService.join(any())).willReturn(user.getId());
        doNothing().when(userService).deleteUser(user.getId());
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1")).andExpect(status().isNoContent()).andDo(print());


    }
    @DisplayName("회원 정보 수정")
    @WithMockUser(username = "test",password = "123",roles = {"MENTOR"})
    @Test
    public void updateUser() throws Exception{
        User user = User.builder()
                .name("user1")

                .password("123")
                .role(Role.ROLE_MENTOR)
                .img(null)
                .build();
        given(userService.join(any())).willReturn(user.getId());
        User newUser = User.builder()
                .name("user2")
                .email("user1@email.com")
                .password("12345")
                .role(Role.ROLE_MENTOR)
                .img(null)
                .build();;
        given(userService.findById(any())).willReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser))).andExpect(status().isOk()).andDo(print());



    }
}
