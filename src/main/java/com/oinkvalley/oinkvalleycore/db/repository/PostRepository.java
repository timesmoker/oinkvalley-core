package com.oinkvalley.oinkvalleycore.db.repository;

import com.oinkvalley.oinkvalleycore.db.domain.Post;
import com.oinkvalley.oinkvalleycore.dto.projection.PostBaseProjection;
import com.oinkvalley.oinkvalleycore.dto.projection.PostCommentCountProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 게시판 종류에 따라 게시글 조회
    @Query("""
    SELECT p.id AS id, p.title AS title, u.username AS username, p.createdAt AS createdAt
    FROM Post p
    JOIN p.user u
    WHERE p.boardType = :boardType
    ORDER BY p.id DESC
    """)
    Page<PostBaseProjection> findPostBaseList(
            @Param("boardType") String boardType,
            Pageable pageable
    );

    //게시글 목록으로 댓글수 조회
    @Query("""
    SELECT p.id AS postId, COUNT(c) AS commentCount
    FROM Post p
    LEFT JOIN p.comments c
    WHERE p.id IN :postIds
    GROUP BY p.id
    """)
    List<PostCommentCountProjection> countCommentsByPostIds(@Param("postIds") List<Long> postIds);


}
