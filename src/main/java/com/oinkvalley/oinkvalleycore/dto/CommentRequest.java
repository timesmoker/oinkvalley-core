package com.oinkvalley.oinkvalleycore.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CommentRequest {

    @NotNull  // 게시글 ID
    private Long postId;

    @NotNull  // 내용물
    private Map<String, Object> content;

}
