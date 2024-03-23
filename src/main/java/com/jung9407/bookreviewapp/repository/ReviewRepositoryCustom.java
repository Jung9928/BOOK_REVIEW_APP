package com.jung9407.bookreviewapp.repository;

import com.jung9407.bookreviewapp.model.dto.requestDTO.ReviewSearchConditionDTO;
import com.jung9407.bookreviewapp.model.entity.ReviewEntity;
import com.jung9407.bookreviewapp.util.ReviewSource;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.jung9407.bookreviewapp.model.entity.QReviewEntity.reviewEntity;

@RequiredArgsConstructor
@Repository
public class ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public Page<ReviewEntity> findAllBySearchCondition(Pageable pageable, ReviewSearchConditionDTO reviewSearchConditionDTO) {
        JPAQuery<ReviewEntity> query =
                queryFactory.selectFrom(reviewEntity).where(
                        searchBookId(reviewSearchConditionDTO.getBookId()),
                        searchReviewSite(reviewSearchConditionDTO.getReviewSite())
                );

        long total = query.stream().count();            // 전체 리뷰 데이터 카운트 후, 아래에서 조건 처리

        List<ReviewEntity> results = query
                .where(searchBookId(reviewSearchConditionDTO.getBookId()),
                        searchReviewSite(reviewSearchConditionDTO.getReviewSite()))
//                .offset(pageable.getOffset())
                .offset(((long)(pageable.getPageNumber()-1)* pageable.getPageSize()))
                .limit(pageable.getPageSize())
                .orderBy(reviewEntity.reviewDate.desc())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }

    private BooleanExpression searchBookId(int book_id) {
        return reviewEntity.bookId.eq(book_id);
    }

    private BooleanExpression searchReviewSite(String review_site) {
        if(StringUtils.hasLength(review_site)) {
            if(ReviewSource.YES24.equals(review_site)) {
                return reviewEntity.reviewSite.contains(review_site);
            }
        }

        return null;
    }
}
