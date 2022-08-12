package project.kiyobackend.user.adapter.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.kiyobackend.review.domain.domain.Score;
import project.kiyobackend.store.adapter.presentation.dto.review.ReviewImageDto;
import project.kiyobackend.store.adapter.presentation.dto.review.ReviewResponseDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserReviewResponse {

    @Schema(description = "리뷰 아이디")
    private Long reviewId;
    @Schema(description = "유저가 리뷰 남긴 가게 이름",example = "버거킹")
    private String storeName;
    @Schema(example = "성동구 왕십리")
    private String address;
    @Schema(example = "HIGH")
    private Score score;
    @Schema(example = "직원들이 너무 불친절해요..")
    private String content;
    private StoreImageResponseDto storeImage;
    private List<ReviewImageDto> reviewImages;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime updateTime;
}
