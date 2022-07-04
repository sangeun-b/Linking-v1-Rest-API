package com.example.linkingrest.user;

import com.example.linkingrest.user.domain.User;
import com.example.linkingrest.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Nested
    @DisplayName("회원 가입")
    @Transactional
    class join {
        @DisplayName("회원 가입 성공")
        @Test
        void Save() {
            User user = User.builder()
                    .name("user1")
                    .email("user1@email.com")
                    .password("12345")
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
    @DisplayName("회원 조회")
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
                    .name("user2")
                    .email("user1@email.com")
                    .password("12345")
                    .img(null)
                    .build();
            userRepository.save(user);
            //when
            List<User> userList = userRepository.findByName(user.getName());

            //then
            User findUser = userList.get(0);
            assertEquals(userList.size(), 1);
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
            List<User> userList = userRepository.findByEmail(user.getEmail());

            //then
            User findUser = userList.get(0);
            assertEquals(userList.size(), 1);
            assertEquals(user.getEmail(), findUser.getEmail());
        }
    }
    @DisplayName("회원 삭제")
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

