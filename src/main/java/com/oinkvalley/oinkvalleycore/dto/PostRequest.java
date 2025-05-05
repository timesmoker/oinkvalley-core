package com.oinkvalley.oinkvalleycore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class PostRequest {

    @NotBlank
    private String title;

    @NotNull
    private Map<String, Object> content;

}
