package com.lightlyshiny.backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JWTUtilities {
    private final JWTProperties properties;
    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    public String generate(String email, String role) {
        return JWT
                .create()
                .withSubject(email)
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + properties.getExpiration()))
                .sign(algorithm);
    }

    public DecodedJWT decode(String token) {
        try {
            return verifier.verify(token);
        } catch (JWTVerificationException exception) {
            return null;
        }
    }
}