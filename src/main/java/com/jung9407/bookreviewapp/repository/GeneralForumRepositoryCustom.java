package com.jung9407.bookreviewapp.repository;

import com.jung9407.bookreviewapp.model.dto.requestDTO.GeneralForumSearchConditionDTO;
import com.jung9407.bookreviewapp.model.entity.GeneralForumEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.jung9407.bookreviewapp.model.entity.QGeneralForumEntity.generalForumEntity;

@RequiredArgsConstructor
@Repository
public class GeneralForumRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public Page<GeneralForumEntity> findAllBySearchCondition(Pageable pageable, GeneralForumSearchConditionDTO generalForumSearchConditionDTO) {

        JPAQuery<GeneralForumEntity> query =
                queryFactory.selectFrom(generalForumEntity).where(
                        searchKeywords(generalForumSearchConditionDTO.getSearchCategory(), generalForumSearchConditionDTO.getSearchValue())
                );

        long total = query.stream().count();

        List<GeneralForumEntity> results = query
                .where(
                        searchKeywords(generalForumSearchConditionDTO.getSearchCategory(), generalForumSearchConditionDTO.getSearchValue()))
                .offset((long)(pageable.getPageNumber()-1) * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .orderBy(generalForumEntity.registeredAt.desc())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }

    private BooleanExpression searchKeywords(String searchCategory, String searchValue) {
        if("title".equals(searchCategory)) {
            if(StringUtils.hasLength(searchValue)) {
                return generalForumEntity.title.contains(searchValue);
            }
        }
        else if("author".equals(searchCategory)) {
            if(StringUtils.hasLength(searchValue)) {
                return generalForumEntity.member.memberId.contains(searchValue);
            }
        }
        else if("content".equals(searchCategory)) {
            if(StringUtils.hasLength(searchCategory)) {
                return generalForumEntity.content.in(searchValue.getBytes(StandardCharsets.UTF_8));
            }
        }

        return null;
    }
}
