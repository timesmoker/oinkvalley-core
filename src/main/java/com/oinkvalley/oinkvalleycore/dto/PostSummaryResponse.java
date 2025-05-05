package com.oinkvalley.oinkvalleycore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class PostSummaryResponse {
    private Long id;
    private String title;
    private String authorName;
    private Instant createdAt;
    private Long commentCount;
}
