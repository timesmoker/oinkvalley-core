package com.oinkvalley.oinkvalleycore.dto.board;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;

@Getter
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String username;
    private Map<String, Object> content;
    private Instant createdAt;
}
