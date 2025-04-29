package com.oinkvalley.oinkvalleycore.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ImageResponse {
    private Long id;
    private String filename;
    private String url;
    private Instant uploadedAt;
}
