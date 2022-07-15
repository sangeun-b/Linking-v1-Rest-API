package com.example.linkingrest.user;

import com.example.linkingrest.user.domain.Role;
import com.example.linkingrest.user.domain.User;
import com.example.linkingrest.user.repository.UserRepository;
import com.example.linkingrest.user.service.UserService;
import com.example.linkingrest.error.exception.EmailSignupFailedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserMockServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Nested
    @DisplayName("회원 가입 테스트")
    @Transactional
    class join {
        @DisplayName("회원 가입 성공")
        @Test
        public void join() throws Exception {
            //given
           User user = User.builder()
                    .name("user1")
                    .email("user1@email.com")
                    .password("123")
                    .role(Role.ROLE_MENTOR)
                    .img(null)
                    .build();

            //mocking
            given(userRepository.save(any()))
                    .willReturn(user);
            given(userRepository.findById(user.getId()))
                    .willReturn(Optional.ofNullable(user));
            //when
            Long newUserId = userService.join(user);

            //then
            User findUser = userRepository.findById(newUserId).orElseThrow();
            assertEquals(user.getId(), findUser.getId());
            assertEquals(user.getName(), findUser.getName());
        }

        @DisplayName("회원 가입 실패 - 이메일 중복")
        @Test
        public void duplicatedEmail_Fail() throws Exception {
            //given
            User user1 = User.builder()
                    .name("user1")
                    .email("user1@email.com")
                    .password("123")
                    .role(Role.ROLE_MENTOR)
                    .img(null)
                    .build();
            User user2 = User.builder()
                    .email("user1@email.com")
                    .password("2222")
                    .name("user1")
                    .role(Role.ROLE_MENTOR)
                    .img(null)
                    .build();
            Long userId = userService.join(user1);
            //mocking
//            given(userRepository.save(any()))
//                    .willReturn(user1);
            given(userRepository.findByEmail(user1.getEmail()))
                    .willReturn(Optional.ofNullable(user1));

            //when & then
            assertThrows(EmailSignupFailedException.class, () -> userService.join(user2));
        }
    }

    @Nested
    @DisplayName("회원 조회 테스트")
    class find {
        @DisplayName("모든 회원 조회")
        @Test
        public void findAll() throws Exception{
            //given
            User user1 = User.builder()
                    .name("user1")
                    .email("user1@email.com")
                    .password("123")
                    .role(Role.ROLE_MENTOR)
                    .img(null)
                    .build();
            User user2 = User.builder()
                    .email("user1@email.com")
                    .password("2222")
                    .name("user1")
                    .role(Role.ROLE_MENTOR)
                    .img(null)
                    .build();
            User[] users = {user1, user2};
            userService.join(user1);
            userService.join(user2);
             //mocking
            given(userRepository.findAll()).willReturn(List.of(users));

            //when
            List<User> findUsers = userService.findUsers();
            //then
            assertEquals(2,findUsers.size());

        }
        @DisplayName("회원 ID로 조회")
        @Test
        public void findUserById() throws Exception{
            //given
            User user = User.builder()
                    .name("user1")
                    .email("user1@email.com")
                    .password("123")
                    .role(Role.ROLE_MENTOR)
                    .img(null)
                    .build();
            //mocking
            given(userRepository.findById(any())).willReturn(Optional.ofNullable(user));
            //when
            User findUser = userService.findById(user.getId());
            //then
            assertEquals(user,findUser);

        }
    }
    @DisplayName("회원 삭제 테스트")
    @Transactional
    @Test
    public void deleteUser() throws Exception{
        //given
        User user = User.builder()
                .name("user1")
                .email("user1@email.com")
                .password("123")
                .role(Role.ROLE_MENTOR)
                .img(null)
                .build();
        // mocking
        given(userRepository.findById(any())).willReturn(Optional.ofNullable(user));
        doNothing().when(userRepository).delete(any());
        // when
        userService.deleteUser(user.getId());

        // then
        verify(userRepository, times(1)).delete(any());

    }
    @DisplayName("회원 정보 수정")
    @Transactional
    @Test
    public void updateUser() throws Exception{
        //given
        User user = User.builder()
                .name("user1")
                .email("user1@email.com")
                .password("123")
                .role(Role.ROLE_MENTOR)
                .img(null)
                .build();
        User newUser = User.builder().name("USER").build();
        MultipartFile file = null;
        // mocking
        given(userRepository.findById(any())).willReturn(Optional.ofNullable(user));
        // when
        userService.updateUser(newUser,user.getId(),file);
        // then
        User findUser = userRepository.findById(newUser.getId()).orElse(null);
        assertEquals(newUser.getId(), findUser.getId());
        assertEquals(newUser.getName(),findUser.getName());

    }

}
