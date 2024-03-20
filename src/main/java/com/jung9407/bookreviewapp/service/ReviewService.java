package com.jung9407.bookreviewapp.service;

import com.jung9407.bookreviewapp.model.dto.ReviewPaginationDTO;
import com.jung9407.bookreviewapp.model.dto.requestDTO.ReviewSearchConditionDTO;
import com.jung9407.bookreviewapp.model.dto.responseDTO.ReviewPagingResponseDTO;
import com.jung9407.bookreviewapp.model.dto.responseDTO.ReviewResponseDTO;
import com.jung9407.bookreviewapp.model.entity.ReviewEntity;
import com.jung9407.bookreviewapp.repository.ReviewRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepositoryCustom reviewRepositoryCustom;

    public ReviewPagingResponseDTO<List<ReviewResponseDTO>> getReviewList(Pageable pageable, ReviewSearchConditionDTO reviewSearchConditionDTO) {
        List<ReviewResponseDTO> reviewResponseDTOList = new ArrayList<>();

        Page<ReviewEntity> reviewEntities = reviewRepositoryCustom.findAllBySearchCondition(pageable, reviewSearchConditionDTO);

        for (ReviewEntity reviewEntity : reviewEntities) {
            ReviewResponseDTO reviewResponseDTO = ReviewResponseDTO.builder()
                    .book_id(reviewEntity.getBook_id())
                    .review_id(reviewEntity.getReview_id())
                    .review_title(reviewEntity.getReview_title())
                    .review_rating(reviewEntity.getReview_rating())
                    .reviewer(reviewEntity.getReviewer())
                    .review_date(reviewEntity.getReviewDate())
                    .review_content(reviewEntity.getReviewContent())
                    .modifiedAt(reviewEntity.getModifiedAt())
                    .review_site(reviewEntity.getReviewSite())
                    .build();

            reviewResponseDTOList.add(reviewResponseDTO);
        }

        ReviewPaginationDTO reviewPaginationDTO = new ReviewPaginationDTO(
                (int) reviewEntities.getTotalElements()
                , pageable.getPageNumber() + 1
                , pageable.getPageSize()
                , 5
        );

        return ReviewPagingResponseDTO.OK(reviewResponseDTOList, reviewPaginationDTO);
    }
}
