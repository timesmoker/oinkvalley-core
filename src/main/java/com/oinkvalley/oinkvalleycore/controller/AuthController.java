package com.oinkvalley.oinkvalleycore.controller;

import com.oinkvalley.oinkvalleycore.services.AuthService;
import com.oinkvalley.oinkvalleycore.db.domain.User;
import com.oinkvalley.oinkvalleycore.db.repository.UserRepository;
import com.oinkvalley.oinkvalleycore.dto.ErrorResponse;
import com.oinkvalley.oinkvalleycore.dto.LoginRequest;
import com.oinkvalley.oinkvalleycore.dto.SignUpRequest;
import com.oinkvalley.oinkvalleycore.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            Cookie cookie = authService.buildAuthCookie(token);
            response.addCookie(cookie);
            return ResponseEntity.ok("Login successful");
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }


}
