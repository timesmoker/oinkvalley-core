package com.oinkvalley.oinkvalleycore.services;

import com.oinkvalley.oinkvalleycore.db.domain.User;
import com.oinkvalley.oinkvalleycore.db.repository.UserRepository;
import com.oinkvalley.oinkvalleycore.dto.auth.LoginRequest;
import com.oinkvalley.oinkvalleycore.dto.auth.SignUpRequest;
import com.oinkvalley.oinkvalleycore.dto.auth.UserResponse;
import com.oinkvalley.oinkvalleycore.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Value("${jwt.expiration:2592000}")
    private int jwtExpiration;

    @Value("${DEV_MODE:false}")
    private boolean devMode;

    public void signup(SignUpRequest request) {
        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .roles(
                        (request.roles() == null || request.roles().isEmpty()) ?
                                List.of("USER") : request.roles()
                )
                .build();

        userRepository.save(user);
    }

    public String login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtUtil.generateToken(user.getId().toString(), user.getRoles());
    }

    public ResponseCookie buildAuthCookie(String token) {
        // Explicit SameSite to avoid browser default differences.
        // Lax works for same-site requests (incl. subdomains) while preventing most CSRF.
        return ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(!devMode) // default: true in non-dev
                .path("/")
                .maxAge(jwtExpiration)
                .sameSite("Lax")
                .build();
    }

    public UserResponse getCurrentUserInfo(User user) {
        return new UserResponse(user.getEmail(), user.getUsername());
    }

}
