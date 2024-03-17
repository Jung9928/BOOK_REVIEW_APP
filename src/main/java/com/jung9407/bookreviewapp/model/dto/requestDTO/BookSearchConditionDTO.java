package com.jung9407.bookreviewapp.model.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookSearchConditionDTO {

    private String searchSubCategory;       // 검색 조건인 서브 카테고리
    private String searchValue;             // 검색 값
    private String searchMainCategory;      // 검색 조건인 메인 카테고리
}