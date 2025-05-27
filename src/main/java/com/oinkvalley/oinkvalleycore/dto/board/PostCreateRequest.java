package com.oinkvalley.oinkvalleycore.dto.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record PostCreateRequest(
        @NotBlank
        String title,

        @NotNull
        Map<String, Object> content
) {}

