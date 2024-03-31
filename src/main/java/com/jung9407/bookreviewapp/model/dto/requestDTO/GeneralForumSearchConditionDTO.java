package com.jung9407.bookreviewapp.model.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeneralForumSearchConditionDTO {

    private String searchCategory;        // 검색 조건인 제목/작성자/내용 카테고리
    private String searchValue;           // 검색 값
}
