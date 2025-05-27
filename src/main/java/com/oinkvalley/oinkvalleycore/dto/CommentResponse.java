package com.oinkvalley.oinkvalleycore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;

@Getter
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String authorName;
    private Map<String, Object> content;
    private Instant createdAt;
}
