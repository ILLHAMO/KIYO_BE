package project.kiyobackend.store.presentation.dto.store;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecentKeywordRequest {

    private String key;
    private String keyword;
}
