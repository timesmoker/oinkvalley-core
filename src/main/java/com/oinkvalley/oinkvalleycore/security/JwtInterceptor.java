package com.oinkvalley.oinkvalleycore.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    public JwtInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 헤더에서 JWT 꺼내기
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            return false;
        }

        // 2. JWT 검증
        String token = authHeader.substring(7);  // "Bearer " 자르기

        try {
            List<String> roles = jwtUtil.getRolesFromToken(token);  // roles 꺼내기

            // 3. 필요한 권한 체크 (예시: ADMIN)
            if (!roles.contains("ADMIN")) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);  // 403
                return false;
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 401
            return false;
        }

        return true;  // 통과!
    }
}
