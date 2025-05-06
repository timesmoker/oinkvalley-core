package com.oinkvalley.oinkvalleycore.dto;

import java.util.List;

public class LoginResponse {
    private String token;
    private Long id;
    private String email;
    private List<String> roles;

    public LoginResponse(String token, Long id, String email, List<String> roles) {
        this.token = token;
        this.id = id;
        this.email = email;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }
}
