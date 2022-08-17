package com.example.linkingrest.user;

import com.example.linkingrest.user.domain.Role;
import com.example.linkingrest.user.domain.User;
import com.example.linkingrest.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
// @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Nested
    @DisplayName("회원 가입 테스트")
    @Transactional
    class join {
        @DisplayName("회원 가입 성공")
        @Test
        void save() {
            User user = User.builder()
                    .name("user1")
                    .email("user1@email.com")
                    .password("12345")
                    .role(Role.ROLE_MENTOR)
                    .img(null)
                    .build();
            userRepository.save(user);
            Long userId = user.getId();
            User findUser = userRepository.findById(userId).orElse(null);

            assertNotNull(findUser);
            assertEquals(findUser.getId(), userId);
        }
    }

    @Nested
    @DisplayName("회원 조회 테스트")
    class find {
        @DisplayName("회원 목록 조회 성공")
        @Test
        void findAll() {
            User user = User.builder()
                    .name("user2")
                    .email("user1@email.com")
                    .password("12345")
                    .img(null)
                    .build();

            userRepository.save(user);

            List<User> userList = userRepository.findAll();

            assertEquals(userList.size(), 1);
        }

        @DisplayName("회원 이름 조회 성공")
        @Test
        public void findByNameS() throws Exception {
            //given
            User user = User.builder()
                    .name("user1")
                    .email("user1@email.com")
                    .password("12345")
                    .img(null)
                    .build();
            userRepository.save(user);
            //when
            User findUser = userRepository.findByName(user.getName()).orElse(null);

            //then
            assertNotNull(findUser);
            assertEquals(user.getName(), findUser.getName());
        }
        @DisplayName("회원 이메일 조회 성공")
        @Test
        public void findByEmailS() throws Exception {
            //given
            User user = User.builder()
                    .name("user2")
                    .email("user1@email.com")
                    .password("12345")
                    .img(null)
                    .build();
            userRepository.save(user);
            //when
            User findUser = userRepository.findByEmail(user.getEmail()).orElse(null);

            //then
            assertNotNull(findUser);
            assertEquals(user.getEmail(), findUser.getEmail());
        }
    }
    @DisplayName("회원 삭제")
    @Transactional
    @Test
    public void deleteUser() {
        User user = User.builder()
                .name("user2")
                .email("user1@email.com")
                .password("12345")
                .img(null)
                .build();
        userRepository.save(user);

        userRepository.delete(user);

        assertEquals(userRepository.findAll().size(),0);
    }



}

