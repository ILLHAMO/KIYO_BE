package project.kiyobackend.user.application.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.kiyobackend.review.domain.domain.Score;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserReviewResponseDto {

    private Long reviewId;
    private String storeName;
    private String address;
    private Score score;
    private String content;
}
