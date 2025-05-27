package com.oinkvalley.oinkvalleycore.dto;

import java.time.Instant;
import java.util.Map;

public record PostDetailResponse(
        Long id,
        String title,
        Map<String, Object> content,
        String authorName,
        Instant createdAt,
        Instant updatedAt
) {}