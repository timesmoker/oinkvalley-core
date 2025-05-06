package com.oinkvalley.oinkvalleycore.controller;

import com.oinkvalley.oinkvalleycore.db.domain.User;
import com.oinkvalley.oinkvalleycore.db.repository.UserRepository;
import com.oinkvalley.oinkvalleycore.dto.ErrorResponse;
import com.oinkvalley.oinkvalleycore.dto.LoginRequest;
import com.oinkvalley.oinkvalleycore.dto.LoginResponse;
import com.oinkvalley.oinkvalleycore.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder; // 이거!!
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder; // 추가!

    public AuthController(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash())); // 여기 수정
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Arrays.asList("USER"));
        }
        userRepository.save(user);
        return ResponseEntity.ok("Signup successful!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
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


    @PostMapping("/grant-admin/{userId}")
    public ResponseEntity<?> grantAdminRole(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getRoles().contains("ADMIN")) {
            user.getRoles().add("ADMIN");
            userRepository.save(user);
        }

        return ResponseEntity.ok("Granted ADMIN role to user " + userId);
    }

}
