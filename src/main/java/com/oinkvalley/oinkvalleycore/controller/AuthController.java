package com.oinkvalley.oinkvalleycore.controller;

import com.oinkvalley.oinkvalleycore.db.domain.User;
import com.oinkvalley.oinkvalleycore.services.AuthService;
import com.oinkvalley.oinkvalleycore.dto.ErrorResponse;
import com.oinkvalley.oinkvalleycore.dto.auth.LoginRequest;
import com.oinkvalley.oinkvalleycore.dto.auth.SignUpRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequest request) {
        authService.signup(request);
        return ResponseEntity.ok("Signup successful!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        try {
            String token = authService.login(request);
            response.addHeader(HttpHeaders.SET_COOKIE, authService.buildAuthCookie(token).toString());
            return ResponseEntity.ok("Login successful");
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> me(@AuthenticationPrincipal User user) {
        System.out.println("User: " + user);
        return ResponseEntity.ok(authService.getCurrentUserInfo(user));
    }

}
