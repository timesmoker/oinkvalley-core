package com.oinkvalley.oinkvalleycore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class PostRequest {

    @NotBlank  // 제목은 비어있으면 안됨!
    private String title;

    @NotNull
    private Integer user_id;

    @NotNull  // content는 JSON 형태로!
    private Map<String, Object> content;

    @NotBlank  // 게시판 타입도 필수!
    private String boardType;
}
