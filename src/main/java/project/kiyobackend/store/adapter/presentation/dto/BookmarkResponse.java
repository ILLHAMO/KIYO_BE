package project.kiyobackend.store.adapter.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
public class BookmarkResponse {

    private int bookmarkCount;
    private boolean isBooked;

    @Builder
    public BookmarkResponse(int bookmarkCount, boolean isBooked) {
        this.bookmarkCount = bookmarkCount;
        this.isBooked = isBooked;
    }
}
