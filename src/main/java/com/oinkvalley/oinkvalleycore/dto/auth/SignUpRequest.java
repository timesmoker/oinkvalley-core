package com.oinkvalley.oinkvalleycore.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record SignUpRequest(
        @NotBlank
        @Email
        String email,

        @NotBlank
        String password,

        @NotBlank
        String username,

        List<String> roles
) {}
