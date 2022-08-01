package com.example.linkingrest;

import com.example.linkingrest.config.security.JwtProvider;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test-jwt")
class LinkingRestApplicationTests {

    @Test
    void contextLoads() {
    }

}
