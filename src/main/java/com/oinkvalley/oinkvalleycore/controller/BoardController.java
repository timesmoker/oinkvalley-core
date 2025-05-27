package com.oinkvalley.oinkvalleycore.controller;

import com.oinkvalley.oinkvalleycore.db.domain.User;
import com.oinkvalley.oinkvalleycore.dto.board.*;
import com.oinkvalley.oinkvalleycore.services.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    // 게시글 작성
    @PostMapping(value = "/{boardType}/posts", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createPost(
            @PathVariable String boardType,
            @Valid @RequestBody PostCreateRequest request,
            @AuthenticationPrincipal User user) {
        boardService.createPost(boardType, request, user);
        return ResponseEntity.ok().body("게시글이 작성되었습니다.");
    }

    // 게시판 요약 정보 조회 --> 게시글 목록
    @GetMapping("/{boardType}/posts")
    public ResponseEntity<?> getPostSummaries(
            @PathVariable String boardType,
            @RequestParam(defaultValue = "0") int page, //0부터 시작
            @RequestParam(defaultValue = "30") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostListResponse> postSummaries = boardService.getPostSummaries(boardType, pageable);
        return ResponseEntity.ok(postSummaries);
    }

    // 게시글 상세 조회
    @GetMapping("/{boardType}/posts/{postId}")
    public ResponseEntity<?> getPostDetail(
            @PathVariable String boardType,
            @PathVariable Long postId
    ) {
        PostDetailResponse response = boardService.getPostDetail(postId);
        return ResponseEntity.ok(response);
    }

    // 게시글 수정
    @PreAuthorize("hasRole('ADMIN') or @ownershipSecurity.isPostOwner(#postId, principal)")
    @PutMapping("/{boardType}/posts/{postId}")
    public ResponseEntity<?> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateRequest request
    ) {
        boardService.updatePost(postId, request);
        return ResponseEntity.ok("게시글이 수정되었습니다.");
    }

    // 게시글 삭제
    @PreAuthorize("hasRole('ADMIN') or @ownershipSecurity.isPostOwner(#postId, principal)")
    @DeleteMapping("/{boardType}/posts/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        boardService.deletePost(postId);
        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }

    // 댓글 작성
    @PostMapping("/{boardType}/posts/{postId}/comments")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentCreateRequest request,
            @AuthenticationPrincipal User user
    ) {
        boardService.createComment(postId, request, user);
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
        Page<CommentResponse> comments = boardService.getComments(postId, pageable);
        return ResponseEntity.ok(comments);
    }

    // 댓글 수정
    @PreAuthorize("hasRole('ADMIN') or @ownershipSecurity.isCommentOwner(#commentId, principal)")
    @PutMapping("/{boardType}/posts/{postId}/comments/{commentId}")
    public ResponseEntity<?> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateRequest request
    ) {
        boardService.updateComment(commentId, request);
        return ResponseEntity.ok("댓글이 수정되었습니다.");
    }

    // 댓글 삭제
    @PreAuthorize("hasRole('ADMIN') or @ownershipSecurity.isCommentOwner(#commentId, principal)")
    @DeleteMapping("/{boardType}/posts/{postId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        boardService.deleteComment(commentId);
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }
}
