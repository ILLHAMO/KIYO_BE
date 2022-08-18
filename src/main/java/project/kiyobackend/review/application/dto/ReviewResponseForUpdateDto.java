package project.kiyobackend.review.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.kiyobackend.review.domain.domain.Score;
import project.kiyobackend.store.adapter.presentation.dto.review.ReviewImageDto;

import java.util.List;

@Data
@AllArgsConstructor
public class ReviewResponseForUpdateDto {

    private String storeName;
    private String address;
    private Score score;
    private String content;
    private List<ReviewImageDto> reviewImages;

}
