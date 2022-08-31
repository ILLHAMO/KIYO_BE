package project.kiyobackend.user.application.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.kiyobackend.review.domain.domain.ReviewImage;
import project.kiyobackend.review.domain.domain.Score;
import project.kiyobackend.store.domain.domain.store.StoreImage;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserReviewResponseDto {

    private Long reviewId;
    private String storeName;
    private String address;
    private Score score;
    private String content;
    private List<StoreImage> storeImage;
    private List<ReviewImage> reviewImages;
    private LocalDateTime updatedDate;
}
