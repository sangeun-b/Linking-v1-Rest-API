package com.example.linkingrest.user;

import com.example.linkingrest.user.controller.CreateUserRequest;
import com.example.linkingrest.user.domain.User;
import com.example.linkingrest.user.repository.UserRepository;
import com.example.linkingrest.user.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserMockServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void join() throws Exception{
        //given
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .name("user1")
                .email("user1@email.com")
                .password("123")
                .img(null)
                .build();
        User user = createUserRequest.toEntity();

        //mocking
        given(userRepository.save(any()))
                .willReturn(user);
        given(userRepository.findById(user.getId()))
                .willReturn(Optional.ofNullable(user));
        //when
        Long newUserId = userService.join(createUserRequest.toEntity());

        //then
        User findUser = userRepository.findById(newUserId).orElseThrow();
        assertEquals(user.getId(),findUser.getId());
        assertEquals(user.getName(),findUser.getName());
    }

    @Test
    public void duplicatedEmail_Fail() throws Exception{
        //given
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .name("user1")
                .email("user1@email.com")
                .password("123")
                .img(null)
                .build();
        User user2 = User.builder()
                .email("user2@email.com")
                .password("2222")
                .name("user1")
                .img(null)
                .build();
        User user = createUserRequest.toEntity();
        //when
        given(userRepository.save(any()))
                .willReturn(user);
        given(userRepository.findById(user.getId()))
                .willReturn(Optional.ofNullable(user));

        //then
        assertThrows(IllegalStateException.class,()->userService.join(user2));
    }

}
