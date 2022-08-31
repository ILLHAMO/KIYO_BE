package project.kiyobackend.store.presentation.dto.store;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.kiyobackend.store.presentation.dto.ImageDto;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserBookmarkResponse {
    private Long id;
    private boolean kids;
    private String name;
    private String address;
    private List<ImageDto> images;
    private int reviewCount;
    private int bookmarkCount;
    private boolean isBooked;

    public UserBookmarkResponse(Long id, boolean kids, List<ImageDto> images, String name, String address, int reviewCount, int bookmarkCount, boolean isBooked) {
        this.id = id;
        this.kids = kids;
        this.name = name;
        this.address = address;
        this.images = images;
        this.reviewCount = reviewCount;
        this.bookmarkCount = bookmarkCount;
        this.isBooked = isBooked;
    }



}
