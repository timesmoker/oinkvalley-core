package com.oinkvalley.oinkvalleycore.db.repository;

import com.oinkvalley.oinkvalleycore.db.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {


}
