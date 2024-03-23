package com.jung9407.bookreviewapp.repository;

import com.jung9407.bookreviewapp.model.dto.requestDTO.BookSearchConditionDTO;
import com.jung9407.bookreviewapp.model.entity.BookEntity;
import com.jung9407.bookreviewapp.util.BookSerialNumber;
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
                        searchMainKeywords(bookSearchConditionDTO.getSearchMainCategory()),
                        searchSubKeywords(bookSearchConditionDTO.getSearchSubCategory(), bookSearchConditionDTO.getSearchValue())
                );

        long total = query.stream().count();        // 전체 도서 데이터 카운트 후, 아래에서 조건 처리

        List<BookEntity> results = query
                .where(searchMainKeywords(bookSearchConditionDTO.getSearchSubCategory()),
                        searchSubKeywords(bookSearchConditionDTO.getSearchSubCategory(), bookSearchConditionDTO.getSearchValue()))
                // 페이지 번호
                // 프론트에서 맨 처음 로딩 시, 1페이지 값을 전달하므로 getPageNumber에 -1하여 offset 첫 값을 0설정
                // ex) limit(0, 10) -> limit(10, 10) -> limit(20, 10)
                .offset(((long)(pageable.getPageNumber()-1) * pageable.getPageSize()))
                .limit(pageable.getPageSize())      // 페이지에 표시할 도서 갯수
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

    private BooleanExpression searchMainKeywords(String searchMainCategory) {
        if(BookSerialNumber.COMPUTER_ENGINEERING.getBookSerialNumber().equals(searchMainCategory)) {
            return bookEntity.main_category.contains(searchMainCategory);
        }
        else if(BookSerialNumber.COMPUTER_BEGINNER.getBookSerialNumber().equals(searchMainCategory)) {
            return bookEntity.main_category.contains(searchMainCategory);
        }
        else if(BookSerialNumber.MOBILE_PROGRAMMING.getBookSerialNumber().equals(searchMainCategory)) {
            return bookEntity.main_category.contains(searchMainCategory);
        }
        else if(BookSerialNumber.PROGRAMMING_LANGUAGE.getBookSerialNumber().equals(searchMainCategory)) {
            return bookEntity.main_category.contains(searchMainCategory);
        }
        else if(BookSerialNumber.WEBSITE.getBookSerialNumber().equals(searchMainCategory)) {
            return bookEntity.main_category.contains(searchMainCategory);
        }
        else if(BookSerialNumber.OS_DATABASE.getBookSerialNumber().equals(searchMainCategory)) {
            return bookEntity.main_category.contains(searchMainCategory);
        }
        else if(BookSerialNumber.GAME.getBookSerialNumber().equals(searchMainCategory)) {
            return bookEntity.main_category.contains(searchMainCategory);
        }
        else if(BookSerialNumber.NETWORK_HACKING_SECURITY.getBookSerialNumber().equals(searchMainCategory)) {
            return bookEntity.main_category.contains(searchMainCategory);
        }
        else if(BookSerialNumber.GRAPHIC_DESIGN_MULTIMEDIA.getBookSerialNumber().equals(searchMainCategory)) {
            return bookEntity.main_category.contains(searchMainCategory);
        }
        else {
            return null;
        }
    }
}
