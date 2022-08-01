package project.kiyobackend.store.adapter.presentation.dto.store;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RecentSearchResponseDto {
    private String recent_keyword;
    private Long localDateTime;
}
