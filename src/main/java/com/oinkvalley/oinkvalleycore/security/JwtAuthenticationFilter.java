package com.oinkvalley.oinkvalleycore.security;

import com.oinkvalley.oinkvalleycore.db.domain.User;
import com.oinkvalley.oinkvalleycore.db.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                // 1. 서명, 만료 체크
                jwtUtil.validateToken(token);

                // 2. 토큰에서 사용자 정보 꺼내기
                String userId = jwtUtil.getUserIdFromToken(token);
                List<String> roles = jwtUtil.getRolesFromToken(token);

                // DB에서 User 조회!
                User user = userRepository.findById(Long.valueOf(userId))
                        .orElseThrow(() -> new RuntimeException("User not found"));

                // 인증 객체 생성 (principal에 User 넣기)
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, null,
                                roles.stream()
                                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                                        .toList());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                // 토큰 에러 → 그냥 넘어가서 시큐리티가 401 처리
            }
        }

        filterChain.doFilter(request, response);
    }
}
