package com.oinkvalley.oinkvalleycore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
public class PostSummaryResponse {
    private Long id;
    private String title;
    private String username;
    private Instant createdAt;
    private Long commentCount;
}
