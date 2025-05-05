package com.oinkvalley.oinkvalleycore.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class PostDetailResponse {
    private Long id;
    private String title;
    private Map<String, Object> content;
    private String authorName;
    private Instant createdAt;
    private Instant updatedAt;
    private Page<CommentResponse> latestComments;
    private List<ImageResponse> images;
}

