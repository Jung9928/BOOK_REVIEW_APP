package com.jung9407.bookreviewapp.repository;

import com.jung9407.bookreviewapp.model.entity.GeneralForumEntity;
import com.jung9407.bookreviewapp.model.entity.MemberEntity;
import com.jung9407.bookreviewapp.model.entity.RecommendEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GeneralForumRepository extends JpaRepository<GeneralForumEntity, Long> {

    Optional<GeneralForumEntity> findGeneralForumViewCountByPostId(long postId);
}
