package project.kiyobackend.bookmark.adapter.presentation.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkRequestDto {

    private Long storeId;
}
