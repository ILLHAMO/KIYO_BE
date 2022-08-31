package project.kiyobackend.store.presentation.dto.review;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.kiyobackend.review.domain.domain.Score;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewResponseDto {

    private Long id;
    private String reviewerId;
    private String reviewerName;
    private String reviewerImage;
    private Score score;
    private String content;
    private boolean currentUserReview = false;
    private List<ReviewImageDto> reviewImages;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime updatedDate;

}
