package project.kiyobackend.store.adapter.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.kiyobackend.review.domain.domain.Score;

import java.util.List;

@Data
@AllArgsConstructor
public class ReviewResponseDto {

    private Long id;
    private String reviewerName;
    private Score score;
    private List<ReviewImageDto> reviewImages;

}
