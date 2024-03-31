package com.jung9407.bookreviewapp.repository;

import com.jung9407.bookreviewapp.model.entity.GeneralForumEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeneralForumRepository extends JpaRepository<GeneralForumEntity, Long> {
}
