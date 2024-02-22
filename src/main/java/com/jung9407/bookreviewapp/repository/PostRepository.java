package com.jung9407.bookreviewapp.repository;

import com.jung9407.bookreviewapp.model.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
}
