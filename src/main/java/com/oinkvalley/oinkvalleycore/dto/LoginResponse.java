package com.oinkvalley.oinkvalleycore.dto;

import java.util.List;

public record LoginResponse(
        String token,
        Long id,
        String email,
        List<String> roles
) {}
