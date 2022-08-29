package project.kiyobackend.review.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.kiyobackend.review.domain.domain.Score;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewRequest {

    @Schema(example = "HIGH")
    private Score score;
    @Schema(example = "웨이팅 오래 한 보람이 있습니다!")
    private String content;


}