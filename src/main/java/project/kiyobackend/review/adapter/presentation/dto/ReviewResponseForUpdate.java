package project.kiyobackend.review.adapter.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import project.kiyobackend.review.domain.domain.Score;
import project.kiyobackend.store.adapter.presentation.dto.review.ReviewImageDto;

import java.util.List;

@Data
@AllArgsConstructor
public class ReviewResponseForUpdate {

    @Schema(example = "버거킹")
    private String storeName;
    @Schema(example = "성동구 왕십리")
    private String address;
    @Schema(example = "HIGH")
    private Score score;
    @Schema(example = "생각보다 맛있었어요")
    private String content;
    private List<ReviewImageDto> reviewImages;
}
