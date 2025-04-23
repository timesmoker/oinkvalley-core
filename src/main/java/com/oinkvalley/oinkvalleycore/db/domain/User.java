package com.oinkvalley.oinkvalleycore.db.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.ArrayList;


@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String username;

    private String passwordHash;

    private String provider;

    private String providerId;

    @ElementCollection(fetch = FetchType.EAGER)  // 권한 테이블에서 가져오기
    private List<String> roles = new ArrayList<>();
}
