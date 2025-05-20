package com.lightlyshiny.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RecoverRequestDTO {
    @NotBlank
    @Email
    private String email;
}