package project.kiyobackend.review.application.dto;

import lombok.Builder;
import lombok.Data;
import project.kiyobackend.review.domain.domain.Score;

import java.util.List;

@Data
public class ReviewRequestDto {

    private Score score;
    private String content;
    private List<Long> deleteIds;

    @Builder
    public ReviewRequestDto(Score score, String content, List<Long> deleteIds) {
        this.score = score;
        this.content = content;
        this.deleteIds = deleteIds;
    }
}
