package com.example.linkingrest.user;

import com.example.linkingrest.user.controller.CreateUserRequest;
import com.example.linkingrest.user.controller.UserController;
import com.example.linkingrest.user.domain.User;
import com.example.linkingrest.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void saveUser() throws Exception{
        //given
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .name("user1")
                .email("user1@email.com")
                .password("123")
                .img(null)
                .build();
        User user = createUserRequest.toEntity();
        given(userService.join(any())).willReturn(user.getId());

        //when
        final ResultActions actions = mockMvc.perform(MockMvcRequestBuilders
                .post("/users/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequest)));

        //then
        actions.andDo(print())
                .andExpect(status().isCreated());

    }
}
