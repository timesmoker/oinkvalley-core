package com.oinkvalley.oinkvalleycore.db.repository;

import com.oinkvalley.oinkvalleycore.db.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE p.boardType = :boardType AND (:lastPostId IS NULL OR p.id < :lastPostId) ORDER BY p.id DESC")
    List<Post> findPostsByBoardType(
            @Param("boardType") String boardType,
            @Param("lastPostId") Integer lastPostId,
            Pageable pageable
    );

    @Modifying
    @Query("UPDATE Post p SET p.authorName = :newName WHERE p.user.id = :userId")
    void updateAuthorName(@Param("userId") Long userId, @Param("newName") String newName);

}
