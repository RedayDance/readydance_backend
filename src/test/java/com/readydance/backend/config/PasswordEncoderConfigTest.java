package com.readydance.backend.config;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PasswordEncoderConfigTest {

    private PasswordEncoder passwordEncoder;

    @Before("")
    public void setUp() throws Exception {
        passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Test
    public void encode() {
        String password = "password";

        String encPassword = passwordEncoder.encode(password);

        assertThat(passwordEncoder.matches(password, encPassword)).isTrue();
        assertThat(encPassword).contains("{bcrypt}");
    }
}

