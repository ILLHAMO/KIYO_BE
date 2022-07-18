package project.kiyobackend.store.adapter.presentation.dto.review;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.kiyobackend.review.domain.domain.Score;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewResponseDto {

    private Long id;
    private String reviewerName;
    private Score score;
    private boolean currentUserReview = false;
    private List<ReviewImageDto> reviewImages;

}
