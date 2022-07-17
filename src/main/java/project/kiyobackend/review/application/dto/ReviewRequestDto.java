package project.kiyobackend.review.application.dto;

import lombok.Builder;
import lombok.Data;
import project.kiyobackend.review.domain.domain.Score;

@Data
public class ReviewRequestDto {

    private Score score;
    private String content;

    @Builder
    public ReviewRequestDto(Score score, String content) {
        this.score = score;
        this.content = content;
    }
}
