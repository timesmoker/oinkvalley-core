package com.oinkvalley.oinkvalleycore.db.repository;

import com.oinkvalley.oinkvalleycore.db.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    // email로 유저 조회
    Optional<User> findByEmail(String email);
    
    //  id로 유저 조회
    Optional<User> findById(Long id);

    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findByEmailWithRoles(String email);

    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findByIdWithRoles(Long id);

}
