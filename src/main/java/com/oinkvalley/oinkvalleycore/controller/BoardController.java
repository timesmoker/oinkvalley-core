package com.oinkvalley.oinkvalleycore.controller;

import com.oinkvalley.oinkvalleycore.db.domain.Post;
import com.oinkvalley.oinkvalleycore.db.domain.User;
import com.oinkvalley.oinkvalleycore.db.repository.PostRepository;
import com.oinkvalley.oinkvalleycore.dto.PostRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/boards")
public class BoardController {

    private final PostRepository postRepository;

    public BoardController(PostRepository postRepository ) {
        this.postRepository = postRepository;
    }

    @PostMapping("/{boardType}/posts")
    @PreAuthorize("hasRole('ADMIN')")
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

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth.getAuthorities());

        return ResponseEntity.ok("Post created in board: " + boardType);
    }

    @GetMapping("/{boardType}/posts")
    public ResponseEntity<?> getPosts(
            @PathVariable String boardType,
            @RequestParam(required = false) Integer lastPostId,
            @RequestParam(defaultValue = "40") int size
    ) {
        Pageable pageable = PageRequest.of(0, size);
        List<Post> posts = postRepository.findPostsByBoardType(boardType, lastPostId, pageable);
        return ResponseEntity.ok(posts);
    }


}
