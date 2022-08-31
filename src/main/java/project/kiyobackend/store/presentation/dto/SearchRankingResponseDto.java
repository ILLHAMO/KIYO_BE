package project.kiyobackend.store.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchRankingResponseDto {
    private String rank_Keyword;
    private double score;
}
