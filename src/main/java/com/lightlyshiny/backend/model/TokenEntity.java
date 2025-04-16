package com.lightlyshiny.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long identifier;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne
    @JoinColumn(nullable = false)
    private UserEntity user;
}