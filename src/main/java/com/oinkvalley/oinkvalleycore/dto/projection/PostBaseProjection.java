package com.oinkvalley.oinkvalleycore.dto.projection;

import java.time.Instant;

public interface PostBaseProjection {
    Long getId();
    String getTitle();
    String getUsername();
    Instant getCreatedAt();
}
