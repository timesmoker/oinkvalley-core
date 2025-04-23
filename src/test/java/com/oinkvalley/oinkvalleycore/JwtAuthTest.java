package com.oinkvalley.oinkvalleycore;

import com.oinkvalley.oinkvalleycore.db.domain.User;
import com.oinkvalley.oinkvalleycore.db.repository.UserRepository;
import com.oinkvalley.oinkvalleycore.security.JwtUtil;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@Commit
@AutoConfigureMockMvc(addFilters = true)  // 🔥 필터 활성화!
public class JwtAuthTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;
    private String jwtToken;


    @Test
    void giveAdminRoleToTimesmoker() {
        // 1. 유저 찾기
        User user = userRepository.findByEmail("timesmoker3526@gmail.com")
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. 권한 추가 (중복 방지!)
        if (!user.getRoles().contains("USER")) {
            user.getRoles().add("USER");
            userRepository.save(user);  // DB에 저장!
        }

        System.out.println("권한 부여 완료! 현재 roles: " + user.getRoles());
    }

}
