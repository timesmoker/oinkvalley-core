package com.oinkvalley.oinkvalleycore.controller;

import com.oinkvalley.oinkvalleycore.db.domain.Comment;
import com.oinkvalley.oinkvalleycore.db.domain.Post;
import com.oinkvalley.oinkvalleycore.db.domain.User;
import com.oinkvalley.oinkvalleycore.db.repository.CommentRepository;
import com.oinkvalley.oinkvalleycore.db.repository.PostRepository;
import com.oinkvalley.oinkvalleycore.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public BoardController(PostRepository postRepository,CommentRepository commentRepository ) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    // 게시글 작성
    @PostMapping(value = "/{boardType}/posts", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createPost(
            @PathVariable String boardType,
            @RequestBody PostRequest request,
            @AuthenticationPrincipal User user)  {

        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setBoardType(boardType);
        post.setUser(user);
        post.setAuthorName(user.getUsername());
        post.setCreatedAt(Instant.now());
        post.setUpdatedAt(Instant.now());

        postRepository.save(post);

        return ResponseEntity.ok(Map.of("message", "Post created in board: " + boardType)); // ✅ JSON 응답
    }

    // 게시판 요약 정보 조회 --> 게시글 목록
    @GetMapping("/{boardType}/posts")
    public ResponseEntity<?> getPostSummaries(
            @PathVariable String boardType,
            @RequestParam(defaultValue = "0") int page,  // 0부터 시작
            @RequestParam(defaultValue = "30") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostSummaryResponse> postSummaries = postRepository.findPostSummaries(boardType, pageable);
        return ResponseEntity.ok(postSummaries);
    }


    // 게시글 상세 조회
    @GetMapping("/{boardType}/posts/{postId}")
    public ResponseEntity<?> getPostDetail(@PathVariable Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Post → DTO 변환
        PostDetailResponse response = new PostDetailResponse();
        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setAuthorName(post.getAuthorName());
        response.setCreatedAt(post.getCreatedAt());
        response.setUpdatedAt(post.getUpdatedAt());

        return ResponseEntity.ok(response);
    }

    // 게시글 수정
    @PreAuthorize("hasRole('ADMIN') or @ownershipSecurity.isPostOwner(#postId, principal)")
    @PutMapping("/{boardType}/posts/{postId}")
    public ResponseEntity<?> updatePost(
            @PathVariable Long postId,
            @RequestBody PostRequest request
    ) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
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
            @RequestBody CommentRequest request,
            @AuthenticationPrincipal User user
    ) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setUser(user);
        comment.setPost(post);
        comment.setAuthorName(user.getUsername());
        comment.setCreatedAt(Instant.now());
        comment.setUpdatedAt(Instant.now());

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
            @RequestBody CommentRequest request
    ){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        comment.setContent(request.getContent());
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
