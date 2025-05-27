package com.oinkvalley.oinkvalleycore.controller;

import com.oinkvalley.oinkvalleycore.db.domain.Comment;
import com.oinkvalley.oinkvalleycore.db.domain.Post;
import com.oinkvalley.oinkvalleycore.db.domain.User;
import com.oinkvalley.oinkvalleycore.db.repository.CommentRepository;
import com.oinkvalley.oinkvalleycore.db.repository.PostRepository;
import com.oinkvalley.oinkvalleycore.dto.*;
import com.oinkvalley.oinkvalleycore.dto.projection.PostBaseProjection;
import com.oinkvalley.oinkvalleycore.dto.projection.PostCommentCountProjection;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    // 게시글 작성
    @PostMapping(value = "/{boardType}/posts", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createPost(
            @PathVariable String boardType,
            @Valid @RequestBody PostRequest request,
            @AuthenticationPrincipal User user) {

        Post post = Post.builder()
                .title(request.title())
                .content(request.content())
                .boardType(boardType)
                .user(user)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        postRepository.save(post);

        return ResponseEntity.ok(Map.of("message", "Post created in board: " + boardType));
    }

    // 게시판 요약 정보 조회 --> 게시글 목록
    @GetMapping("/{boardType}/posts")
    public ResponseEntity<?> getPostSummaries(
            @PathVariable String boardType,
            @RequestParam(defaultValue = "0") int page,  // 0부터 시작
            @RequestParam(defaultValue = "30") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
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

        Page<PostSummaryResponse> resultPage = new PageImpl<>(dtoList, pageable, basePage.getTotalElements());
        return ResponseEntity.ok(resultPage);
    }

    // 게시글 상세 조회
    @GetMapping("/{boardType}/posts/{postId}")
    public ResponseEntity<?> getPostDetail(@PathVariable Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return ResponseEntity.ok(
                new PostDetailResponse(
                        post.getId(),
                        post.getTitle(),
                        post.getContent(),
                        post.getAuthorName(),
                        post.getCreatedAt(),
                        post.getUpdatedAt()
                )
        );
    }

    // 게시글 수정
    @PreAuthorize("hasRole('ADMIN') or @ownershipSecurity.isPostOwner(#postId, principal)")
    @PutMapping("/{boardType}/posts/{postId}")
    public ResponseEntity<?> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody PostRequest request
    ) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setTitle(request.title());
        post.setContent(request.content());
        post.setUpdatedAt(Instant.now());
        postRepository.save(post);

        return ResponseEntity.ok("게시글이 수정되었습니다.");
    }

    // 게시글 삭제
    @PreAuthorize("hasRole('ADMIN') or @ownershipSecurity.isPostOwner(#postId, principal)")
    @DeleteMapping("/{boardType}/posts/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        postRepository.deleteById(postId);
        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }

    // 댓글 작성
    @PostMapping("/{boardType}/posts/{postId}/comments")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal User user
    ) {
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
        return ResponseEntity.ok("댓글이 작성되었습니다.");
    }


    // 댓글 조회
    @GetMapping("/{boardType}/posts/{postId}/comments")
    public ResponseEntity<?> getComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "30") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CommentResponse> comments = commentRepository.findCommentDtosByPostId(postId, pageable);
        return ResponseEntity.ok(comments);
    }

    // 댓글 수정
    @PreAuthorize("hasRole('ADMIN') or @ownershipSecurity.isCommentOwner(#commentId, principal)")
    @PutMapping("/{boardType}/posts/{postId}/comments/{commentId}")
    public ResponseEntity<?> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest request
    ) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        comment.setContent(request.content());
        comment.setUpdatedAt(Instant.now());
        commentRepository.save(comment);

        return ResponseEntity.ok("댓글이 수정되었습니다.");

    }

    // 댓글 삭제
    @PreAuthorize("hasRole('ADMIN') or @ownershipSecurity.isCommentOwner(#commentId, principal)")
    @DeleteMapping("/{boardType}/posts/{postId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        commentRepository.deleteById(commentId);
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }
}

