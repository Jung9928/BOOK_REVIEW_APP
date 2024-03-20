package com.jung9407.bookreviewapp.model.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewSearchConditionDTO {

    private int bookId;                // 도서 데이터 pk값
    private String reviewSite;            // 리뷰 출처
}
