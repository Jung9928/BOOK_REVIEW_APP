package com.jung9407.bookreviewapp.repository;

import com.jung9407.bookreviewapp.model.dto.requestDTO.BookSearchConditionDTO;
import com.jung9407.bookreviewapp.model.entity.BookEntity;
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

import static com.jung9407.bookreviewapp.model.entity.QBookEntity.bookEntity;

// QueryDSL을 사용하므로 인터페이스가 아닌 클래스를 생성하여 JPAQueryFactory을 통해 구현
@RequiredArgsConstructor
@Repository
public class BookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public Page<BookEntity> findAllBySearchCondition(Pageable pageable, BookSearchConditionDTO bookSearchConditionDTO) {
        JPAQuery<BookEntity> query =
                queryFactory.selectFrom(bookEntity).where(
                        searchMainKeywords(bookSearchConditionDTO.getSearchMainCategory(), bookSearchConditionDTO.getSearchValue()),
                        searchSubKeywords(bookSearchConditionDTO.getSearchSubCategory(), bookSearchConditionDTO.getSearchValue())
                );

        long total = query.stream().count();        // 전체 도서 데이터 카운트 후, 아래에서 조건 처리

        List<BookEntity> results = query
                .where(searchSubKeywords(bookSearchConditionDTO.getSearchSubCategory(), bookSearchConditionDTO.getSearchValue()),
                        searchSubKeywords(bookSearchConditionDTO.getSearchSubCategory(), bookSearchConditionDTO.getSearchValue()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(bookEntity.publishDate.desc())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }

    private BooleanExpression searchSubKeywords(String searchSubCategory, String searchValue) {
        if("title".equals(searchSubCategory)) {
            if (StringUtils.hasLength(searchValue)) {
                return bookEntity.title.contains(searchValue);
            }
        }
        else if("author".equals(searchSubCategory)) {
            if (StringUtils.hasLength(searchValue)) {
                return bookEntity.author.contains(searchValue);
            }
        }
        else if("publisher".equals(searchSubCategory)) {
            if (StringUtils.hasLength(searchValue)) {
                return bookEntity.publisher.contains(searchValue);
            }
        }
        return null;
    }

    private BooleanExpression searchMainKeywords(String searchMainCategory, String searchValue) {
        if("computer-engineering".equals(searchMainCategory)) {
            if (StringUtils.hasLength(searchValue)) {
                return bookEntity.title.contains(searchValue);
            }
        }
        else if("computer-beginner".equals(searchMainCategory)) {
            if (StringUtils.hasLength(searchValue)) {
                return bookEntity.author.contains(searchValue);
            }
        }
        else if("mobile-programming".equals(searchMainCategory)) {
            if (StringUtils.hasLength(searchValue)) {
                return bookEntity.publisher.contains(searchValue);
            }
        }
        else if("programming-language".equals(searchMainCategory)) {
            if (StringUtils.hasLength(searchValue)) {
                return bookEntity.publisher.contains(searchValue);
            }
        }
        else if("website".equals(searchMainCategory)) {
            if (StringUtils.hasLength(searchValue)) {
                return bookEntity.publisher.contains(searchValue);
            }
        }
        else if("os-database".equals(searchMainCategory)) {
            if (StringUtils.hasLength(searchValue)) {
                return bookEntity.publisher.contains(searchValue);
            }
        }
        else if("game".equals(searchMainCategory)) {
            if (StringUtils.hasLength(searchValue)) {
                return bookEntity.publisher.contains(searchValue);
            }
        }
        else if("network-hacking-security".equals(searchMainCategory)) {
            if (StringUtils.hasLength(searchValue)) {
                return bookEntity.publisher.contains(searchValue);
            }
        }
        else if("graphic-design-multimedia".equals(searchMainCategory)) {
            if (StringUtils.hasLength(searchValue)) {
                return bookEntity.publisher.contains(searchValue);
            }
        }

        return null;
    }
}
