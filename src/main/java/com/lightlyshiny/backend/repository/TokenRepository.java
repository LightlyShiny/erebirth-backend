package com.lightlyshiny.backend.repository;

import com.lightlyshiny.backend.model.TokenEntity;
import com.lightlyshiny.backend.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    Optional<TokenEntity> findByToken(String token);
    Optional<TokenEntity> findByUser(UserEntity user);
}