package com.lightlyshiny.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResetRequestDTO {
    @NotBlank
    private String uuid;

    @NotBlank
    private String password;
}