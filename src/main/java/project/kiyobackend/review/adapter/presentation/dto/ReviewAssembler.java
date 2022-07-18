package project.kiyobackend.review.adapter.presentation.dto;

import project.kiyobackend.review.application.dto.ReviewRequestDto;

public class ReviewAssembler {

    public static ReviewRequestDto reviewRequestDto(ReviewRequest reviewRequest)
    {
        return ReviewRequestDto.builder().score(reviewRequest.getScore())
                                  .content(reviewRequest.getContent())
                                  .build();
    }
}
