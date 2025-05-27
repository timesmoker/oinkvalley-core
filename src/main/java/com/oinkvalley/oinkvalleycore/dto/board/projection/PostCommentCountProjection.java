package com.oinkvalley.oinkvalleycore.dto.board.projection;

public interface PostCommentCountProjection {
    Long getPostId();
    Long getCommentCount();
}