package com.oinkvalley.oinkvalleycore.security;

import com.oinkvalley.oinkvalleycore.db.domain.User;
import com.oinkvalley.oinkvalleycore.db.repository.CommentRepository;
import com.oinkvalley.oinkvalleycore.db.repository.PostRepository;
import org.springframework.stereotype.Component;

@Component
public class OwnershipSecurity {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public OwnershipSecurity(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public boolean isPostOwner(Long postId, User user) {
        return postRepository.findById(postId)
                .map(post -> post.getUser().getId().equals(user.getId()))
                .orElse(false);
    }

    public boolean isCommentOwner(Long commentId, User user) {
        return commentRepository.findById(commentId)
                .map(comment -> comment.getUser().getId().equals(user.getId()))
                .orElse(false);
    }
}
