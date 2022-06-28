package com.example.linkingrest.user;

import com.example.linkingrest.user.controller.CreateUserRequest;
import com.example.linkingrest.user.controller.CreateUserResponse;
import com.example.linkingrest.user.controller.UserResponse;
import com.example.linkingrest.user.domain.User;
import com.example.linkingrest.user.repository.UserRepository;
import com.example.linkingrest.user.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;
    @Mock
    UserRepository userRepository;

    private final User user = User.builder()
            .email("user1@email.com")
            .password("12345")
            .name("user1")
            .img(null)
            .posts(null)
            .build();
    private final CreateUserRequest userRequest = CreateUserRequest.builder()
            .email("user1@email.com")
            .password("12345")
            .name("user1")
            .img(null)
            .build();
    @Nested
    @DisplayName("회원 가입 테스트")
    class joinUser {

        @DisplayName("회원 가입 성공")
        @Test
        void joinUser() {
            //given
            given(userRepository.save(any())).willReturn(user);
            //when
            Long savedId = userService.join(userRequest.toEntity());
            //then
            verify(userRepository,atLeastOnce()).save(any(User.class));
            assertEquals(1L,user.getId());

        }
        @DisplayName("이름이 중복이면 실패")
        @Test
        void duplicatedName_fail() {
            //given
            User user1 = User.builder()
                    .email("user1@email.com")
                    .password("12345")
                    .name("user1")
                    .img(null)
                    .build();
            User user2 = User.builder()
                    .email("user2@email.com")
                    .password("2222")
                    .name("user1")
                    .img(null)
                    .build();
            //when
            userService.join(user1);

            //then
            assertThrows(IllegalStateException.class,()->userService.join(user2));

        }
        @DisplayName("메일이 중복이면 실패")
        @Test
        void duplicatedName_success(){
            //given
            User user1 = User.builder()
                    .email("user1@email.com")
                    .password("12345")
                    .name("user1")
                    .img(null)
                    .build();
            User user2 = User.builder()
                    .email("user1@email.com")
                    .password("2222")
                    .name("user2")
                    .img(null)
                    .build();
            //when
            userService.join(user1);
            //then
            assertThrows(IllegalStateException.class,()->userService.join(user2));
        }
    }

    @Nested
    @DisplayName("회원 조회 테스트")
    class find {
        @DisplayName("모든 회원 목록 조회")
        @Test
        void findAll(){
            final User user1 = User.builder()
                    .email("user1@email.com")
                    .password("12345")
                    .name("user1")
                    .img(null)
                    .build();

            List<User> users = userService.findUsers();
            assertEquals(users.size(), 1);
        }
    }
}
