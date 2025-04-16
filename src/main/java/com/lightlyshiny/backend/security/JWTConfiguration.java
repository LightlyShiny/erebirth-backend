package com.lightlyshiny.backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.lightlyshiny.backend.configuration.JWTProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JWTConfiguration {
    private final JWTProperties properties;

    @Bean
    public Algorithm algorithm() {
        return Algorithm.HMAC256(properties.getSecret());
    }

    @Bean
    public JWTVerifier verifier() {
        return JWT.require(algorithm()).build();
    }
}