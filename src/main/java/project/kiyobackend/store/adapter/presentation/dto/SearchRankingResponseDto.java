package project.kiyobackend.store.adapter.presentation.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchRankingResponseDto {
    private String rank_Keyword;
    private double score;
}
