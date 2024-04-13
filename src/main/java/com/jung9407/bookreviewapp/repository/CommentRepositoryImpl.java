package com.jung9407.bookreviewapp.repository;

import com.jung9407.bookreviewapp.model.entity.CommentEntity;
import com.jung9407.bookreviewapp.model.entity.QCommentEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.jung9407.bookreviewapp.model.entity.QCommentEntity.commentEntity;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CustomCommentRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentEntity> findCommentByGeneralForum(Long postId) {
        return queryFactory.selectFrom(commentEntity)
                .leftJoin(commentEntity.parentComment)
                .fetchJoin()
                .where(commentEntity.generalForum.postId.eq(postId))
                .orderBy(commentEntity.parentComment.commentId.asc().nullsFirst())
                .fetch();
    }

    @Override
    public void updateComment(CommentEntity commentEntity) {
        queryFactory.update(QCommentEntity.commentEntity)
                .where(QCommentEntity.commentEntity.commentId.eq(commentEntity.getCommentId()))         // 댓글 ID로 조건 설정
                .set(QCommentEntity.commentEntity.comment, commentEntity.getComment())                  // 댓글 내용 업데이트
                .execute();
    }
}
