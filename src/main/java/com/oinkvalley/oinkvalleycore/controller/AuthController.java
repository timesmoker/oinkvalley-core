package com.oinkvalley.oinkvalleycore.controller;

import com.oinkvalley.oinkvalleycore.db.domain.User;
import com.oinkvalley.oinkvalleycore.db.repository.UserRepository;
import com.oinkvalley.oinkvalleycore.dto.ErrorResponse;
import com.oinkvalley.oinkvalleycore.dto.LoginRequest;
import com.oinkvalley.oinkvalleycore.dto.LoginResponse;
import com.oinkvalley.oinkvalleycore.dto.SignUpRequest;
import com.oinkvalley.oinkvalleycore.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequest request) {
        User user = new User();
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRoles(
                (request.roles() == null || request.roles().isEmpty()) ?
                        List.of("USER") :
                        request.roles()
        );
        userRepository.save(user);
        return ResponseEntity.ok("Signup successful!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Invalid password"));
        }

        // JWT 발급
        String token = jwtUtil.generateToken(user.getId().toString(), user.getRoles());


        return ResponseEntity.ok(new LoginResponse(
                token,
                user.getId(),
                user.getEmail(),
                user.getRoles()
        ));
    }

}
