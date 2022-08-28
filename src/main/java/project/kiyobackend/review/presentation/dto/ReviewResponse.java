package project.kiyobackend.review.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewResponse {

    private boolean success;
    @Schema(example = "21")
    private Long reviewId;
}
