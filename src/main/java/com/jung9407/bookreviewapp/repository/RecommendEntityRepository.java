package com.jung9407.bookreviewapp.repository;

import com.jung9407.bookreviewapp.model.entity.GeneralForumEntity;
import com.jung9407.bookreviewapp.model.entity.MemberEntity;
import com.jung9407.bookreviewapp.model.entity.RecommendEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecommendEntityRepository extends JpaRepository<RecommendEntity, Long> {

    Optional<RecommendEntity> findByMemberAndGeneralForum(MemberEntity member, GeneralForumEntity generalForumEntity);

    @Query(value = "SELECT COUNT(*) FROM RecommendEntity entity WHERE entity.generalForum =:generalForum")
    Long countByGeneralForum(@Param("generalForum") GeneralForumEntity generalForum);

    List<RecommendEntity> findAllByGeneralForum(GeneralForumEntity generalForumEntity);
}
