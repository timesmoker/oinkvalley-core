package com.oinkvalley.oinkvalleycore.services;

import com.oinkvalley.oinkvalleycore.db.domain.User;
import com.oinkvalley.oinkvalleycore.db.repository.UserRepository;
import com.oinkvalley.oinkvalleycore.dto.LoginRequest;
import com.oinkvalley.oinkvalleycore.dto.SignUpRequest;
import com.oinkvalley.oinkvalleycore.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
        User user = userRepository.findByEmailWithRoles(request.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtUtil.generateToken(user.getId().toString(), user.getRoles());
    }

    public Cookie buildAuthCookie(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(!devMode); //default : true
        cookie.setPath("/");
        cookie.setMaxAge(jwtExpiration);
        return cookie;
    }
}
