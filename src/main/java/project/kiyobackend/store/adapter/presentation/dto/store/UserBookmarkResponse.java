package project.kiyobackend.store.adapter.presentation.dto.store;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.kiyobackend.store.adapter.presentation.dto.ImageDto;
import project.kiyobackend.store.domain.domain.store.StoreImage;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

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
