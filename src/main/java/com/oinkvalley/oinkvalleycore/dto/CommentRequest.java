package com.oinkvalley.oinkvalleycore.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CommentRequest {

    @NotNull
    private Long postId;

    @NotNull
    private Map<String, Object> content;

}
