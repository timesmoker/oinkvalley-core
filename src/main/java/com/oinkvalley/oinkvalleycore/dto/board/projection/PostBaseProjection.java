package com.oinkvalley.oinkvalleycore.dto.board.projection;

import java.time.Instant;

public interface PostBaseProjection {
    Long getId();
    String getTitle();
    String getUsername();
    Instant getCreatedAt();
}
