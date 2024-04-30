package com.jung9407.bookreviewapp.controller;

import com.jung9407.bookreviewapp.model.dto.requestDTO.ReviewSearchConditionDTO;
import com.jung9407.bookreviewapp.model.dto.responseDTO.ReviewPagingResponseDTO;
import com.jung9407.bookreviewapp.model.dto.responseDTO.ReviewResponseDTO;
import com.jung9407.bookreviewapp.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/reviewList")
    public ReviewPagingResponseDTO<List<ReviewResponseDTO>> getReviewList(
            @PageableDefault(sort = {"reviewDate"}) Pageable pageable,
            ReviewSearchConditionDTO reviewSearchConditionDTO
    ) {
        log.info("book id : " + reviewSearchConditionDTO.getBookId());
        log.info("review book isbn : " + reviewSearchConditionDTO.getIsbn());
        log.info("review site : " + reviewSearchConditionDTO.getReviewSite());
        return reviewService.getReviewList(pageable, reviewSearchConditionDTO);
    }
}
