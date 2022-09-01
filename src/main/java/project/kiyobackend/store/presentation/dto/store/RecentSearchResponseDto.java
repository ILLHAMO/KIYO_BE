package project.kiyobackend.store.presentation.dto.store;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecentSearchResponseDto {
    private String recent_keyword;
    private String key;
    private Long localDateTime;
}
