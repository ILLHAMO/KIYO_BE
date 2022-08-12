package project.kiyobackend.user.application.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.stylesheets.LinkStyle;
import project.kiyobackend.review.domain.domain.ReviewImage;
import project.kiyobackend.review.domain.domain.Score;
import project.kiyobackend.store.adapter.presentation.dto.review.ReviewImageDto;
import project.kiyobackend.store.domain.domain.store.StoreImage;
import project.kiyobackend.user.adapter.presentation.dto.StoreImageResponseDto;

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
