package com.oinkvalley.oinkvalleycore.db.repository;

import com.oinkvalley.oinkvalleycore.db.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
