package com.oinkvalley.oinkvalleycore.security;

import com.oinkvalley.oinkvalleycore.config.BoardConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;

/**
 * Permits unauthenticated GET requests to /api/boards/{boardType}/** only when {boardType}
 * is included in board.public-boards configuration.
 */
public class PublicBoardGetRequestMatcher implements RequestMatcher {

    private final List<String> publicBoards;

    public PublicBoardGetRequestMatcher(BoardConfig boardConfig) {
        this.publicBoards = boardConfig.getPublicBoards();
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        if (!HttpMethod.GET.matches(request.getMethod())) {
            return false;
        }

        String uri = request.getRequestURI(); // excludes query string
        if (uri == null) {
            return false;
        }

        // Expected: /api/boards/{boardType}/...
        // Split keeps empty segments for leading slash; index 0 is "".
        String[] parts = uri.split("/");
        // ["", "api", "boards", "{boardType}", ...]
        if (parts.length < 4) {
            return false;
        }
        if (!"api".equals(parts[1]) || !"boards".equals(parts[2])) {
            return false;
        }

        String boardType = parts[3];
        if (boardType == null || boardType.isBlank()) {
            return false;
        }

        return publicBoards != null && publicBoards.contains(boardType);
    }
}

