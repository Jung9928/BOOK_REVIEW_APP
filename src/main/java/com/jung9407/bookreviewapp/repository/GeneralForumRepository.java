package com.jung9407.bookreviewapp.repository;

import com.jung9407.bookreviewapp.model.dto.jwt.CustomMemberDetails;
import com.jung9407.bookreviewapp.model.entity.GeneralForumEntity;
import com.jung9407.bookreviewapp.model.entity.MemberEntity;
import com.jung9407.bookreviewapp.model.entity.RecommendEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface GeneralForumRepository extends JpaRepository<GeneralForumEntity, Long> {

    Optional<GeneralForumEntity> findGeneralForumViewCountByPostId(long postId);

    Page<GeneralForumEntity> findGeneralForumEntitiesByMemberId(long id, Pageable pageable);

    @Transactional
    @Modifying
    @Query("Update GeneralForumEntity b set b.viewCount = b.viewCount +1 where b.postId = ?1")
    void countUpView(Long postId);
}
