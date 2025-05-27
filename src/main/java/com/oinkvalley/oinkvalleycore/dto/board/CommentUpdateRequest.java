package com.oinkvalley.oinkvalleycore.dto.board;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record CommentUpdateRequest(
        @NotNull Map<String, Object> content
) {}
