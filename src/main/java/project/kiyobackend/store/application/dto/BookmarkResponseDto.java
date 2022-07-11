package project.kiyobackend.store.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookmarkResponseDto {

    private int bookmarkCount;
    private Boolean isBooked;
}
