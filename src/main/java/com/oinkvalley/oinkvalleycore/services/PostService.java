package com.oinkvalley.oinkvalleycore.services;

import com.oinkvalley.oinkvalleycore.db.domain.Comment;
import com.oinkvalley.oinkvalleycore.db.domain.Post;
import com.oinkvalley.oinkvalleycore.db.domain.User;
import com.oinkvalley.oinkvalleycore.db.repository.CommentRepository;
import com.oinkvalley.oinkvalleycore.db.repository.PostRepository;
import com.oinkvalley.oinkvalleycore.dto.*;
import com.oinkvalley.oinkvalleycore.dto.projection.PostBaseProjection;
import com.oinkvalley.oinkvalleycore.dto.projection.PostCommentCountProjection;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.Instant;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public Page<PostSummaryResponse> getPostSummaries(String boardType, Pageable pageable) {
        Page<PostBaseProjection> basePage = postRepository.findPostBaseList(boardType, pageable);

        List<Long> postIds = basePage.getContent().stream()
                .map(PostBaseProjection::getId)
                .toList();

        Map<Long, Long> commentCountMap = postRepository.countCommentsByPostIds(postIds).stream()
                .collect(Collectors.toMap(
                        PostCommentCountProjection::getPostId,
                        PostCommentCountProjection::getCommentCount
                ));

        List<PostSummaryResponse> dtoList = basePage.getContent().stream()
                .map(base -> PostSummaryResponse.builder()
                        .id(base.getId())
                        .title(base.getTitle())
                        .username(base.getUsername())
                        .createdAt(base.getCreatedAt())
                        .commentCount(commentCountMap.getOrDefault(base.getId(), 0L))
                        .build()
                ).toList();

        return new PageImpl<>(dtoList, pageable, basePage.getTotalElements());
    }

    public PostDetailResponse getPostDetail(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return PostDetailResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .username(post.getUser().getUsername())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    @Transactional
    public void updatePost(Long postId, PostRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setTitle(request.title());
        post.setContent(request.content());
        post.setUpdatedAt(Instant.now());
    }

    @Transactional
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    @Transactional
    public void createPost(String boardType, PostRequest request, User user) {
        Post post = Post.builder()
                .title(request.title())
                .content(request.content())
                .boardType(boardType)
                .user(user)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        postRepository.save(post);
    }

    @Transactional
    public void createComment(Long postId, CommentRequest request, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Comment comment = Comment.builder()
                .content(request.content())
                .user(user)
                .post(post)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        commentRepository.save(comment);
    }

    public Page<CommentResponse> getComments(Long postId, Pageable pageable) {
        return commentRepository.findCommentDtosByPostId(postId, pageable);
    }

    @Transactional
    public void updateComment(Long commentId, CommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        comment.setContent(request.content());
        comment.setUpdatedAt(Instant.now());
    }

    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
