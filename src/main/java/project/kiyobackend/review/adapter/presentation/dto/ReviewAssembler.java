package project.kiyobackend.review.adapter.presentation.dto;

import project.kiyobackend.review.application.dto.ReviewRequestDto;
import project.kiyobackend.review.application.dto.ReviewRequestForUpdateDto;
import project.kiyobackend.review.application.dto.ReviewResponseForUpdateDto;
import project.kiyobackend.review.domain.domain.Review;
import project.kiyobackend.store.adapter.presentation.dto.review.ReviewImageDto;

import java.util.stream.Collectors;

public class ReviewAssembler {

    public static ReviewRequestDto reviewRequestDto(ReviewRequest reviewRequest)
    {
        return ReviewRequestDto.builder().score(reviewRequest.getScore())
                                  .content(reviewRequest.getContent())
                .build();
    }
    public static ReviewRequestForUpdateDto reviewRequestDtoForUpdate(ReviewRequestForUpdate reviewRequestForUpdate)
    {
        return ReviewRequestForUpdateDto.builder().score(reviewRequestForUpdate.getScore())
                .content(reviewRequestForUpdate.getContent())
                .deleteIds(reviewRequestForUpdate.getDeleteIds())
                .build();
    }


    public static ReviewResponseForUpdateDto ReviewResponseForUpdateDto(Review review)
    {
        return new ReviewResponseForUpdateDto(review.getStore().getName(),review.getStore().getAddress(),review.getScore(),review.getContent(),review.getReviewImages().stream().map(ri->new ReviewImageDto(ri.getId(),ri.getPath())).collect(Collectors.toList()));
    }
    public static ReviewResponseForUpdate ReviewResponseForUpdate(ReviewResponseForUpdateDto reviewResponseForUpdateDto)
    {
        return new ReviewResponseForUpdate(reviewResponseForUpdateDto.getStoreName(),reviewResponseForUpdateDto.getAddress(),reviewResponseForUpdateDto.getScore(), reviewResponseForUpdateDto.getContent(), reviewResponseForUpdateDto.getReviewImages());
    }
}
