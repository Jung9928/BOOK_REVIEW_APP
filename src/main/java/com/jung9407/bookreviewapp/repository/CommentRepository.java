package com.jung9407.bookreviewapp.repository;

import com.jung9407.bookreviewapp.model.entity.CommentEntity;
import com.jung9407.bookreviewapp.model.entity.GeneralForumEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long>, CustomCommentRepository {

    @Query("select c  from CommentEntity c left join fetch c.parentComment where c.commentId = :commentId")
    CommentEntity findCommentEntityByCommentIdWithParentComment(@Param("commentId") Long commentId);

    Page<CommentEntity> findCommentEntityByGeneralForum(GeneralForumEntity generalForum, Pageable pageable);
}
