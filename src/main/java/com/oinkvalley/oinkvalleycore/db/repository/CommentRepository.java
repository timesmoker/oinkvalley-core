package com.oinkvalley.oinkvalleycore.db.repository;

import com.oinkvalley.oinkvalleycore.db.domain.Comment;
import com.oinkvalley.oinkvalleycore.dto.CommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 게시글 ID로 댓글 DTO 생성 -> 페이징 처리로 가져오는 댓글수 제한
    @Query("""
    SELECT new com.oinkvalley.oinkvalleycore.dto.CommentResponse(
        c.id, c.authorName, c.content, c.createdAt)
    FROM Comment c
    WHERE c.post.id = :postId
    ORDER BY c.id DESC
    """)
    Page<CommentResponse> findCommentDtosByPostId(
            @Param("postId") Long postId,
            Pageable pageable);

    // TODO: 닉네임 변경 시 댓글 작성자명도 변경
    @Modifying
    @Query("UPDATE Post p SET p.authorName = :newName WHERE p.user.id = :userId")
    void updateAuthorName(@Param("userId") Long userId, @Param("newName") String newName);

}
