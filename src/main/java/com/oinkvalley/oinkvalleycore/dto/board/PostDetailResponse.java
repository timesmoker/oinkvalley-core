package com.oinkvalley.oinkvalleycore.dto.board;

import lombok.Builder;

import java.time.Instant;
import java.util.Map;

@Builder
public record PostDetailResponse(
        Long id,
        String title,
        Map<String, Object> content,
        String username,
        Instant createdAt,
        Instant updatedAt
) {}