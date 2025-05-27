package com.oinkvalley.oinkvalleycore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record PostRequest(
        @NotBlank
        String title,

        @NotNull
        Map<String, Object> content
) {}

