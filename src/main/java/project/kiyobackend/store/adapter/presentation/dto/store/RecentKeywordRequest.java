package project.kiyobackend.store.adapter.presentation.dto.store;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecentKeywordRequest {

    private String key;
    private String keyword;
}
