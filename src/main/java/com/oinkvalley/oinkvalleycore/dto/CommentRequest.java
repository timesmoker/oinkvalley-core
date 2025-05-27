package com.oinkvalley.oinkvalleycore.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record CommentRequest(
        @NotNull
        Map<String, Object> content
) {}
