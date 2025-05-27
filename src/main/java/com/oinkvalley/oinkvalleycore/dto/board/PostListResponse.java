package com.oinkvalley.oinkvalleycore.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
public class PostListResponse {
    private Long id;
    private String title;
    private String username;
    private Instant createdAt;
    private Long commentCount;
}
