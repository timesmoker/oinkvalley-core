package com.oinkvalley.oinkvalleycore.db.repository;

import com.oinkvalley.oinkvalleycore.db.domain.Post;
import com.oinkvalley.oinkvalleycore.dto.PostSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 게시판 종류에 따라 게시글 조회 + 댓글 수
    @Query("""
    SELECT new com.oinkvalley.oinkvalleycore.dto.PostSummaryResponse(
        p.id, p.title, p.authorName, p.createdAt, COUNT(c)
    )
    FROM Post p
    LEFT JOIN p.comments c
    WHERE p.boardType = :boardType
    GROUP BY p.id
    ORDER BY p.id DESC
    """)
    Page<PostSummaryResponse> findPostSummaries(
            @Param("boardType") String boardType,
            Pageable pageable
    );



    // TODO: 닉네임 변경 시 게시글 작성자명도 변경
    @Modifying
    @Query("UPDATE Post p SET p.authorName = :newName WHERE p.user.id = :userId")
    void updateAuthorName(@Param("userId") Long userId, @Param("newName") String newName);

}
