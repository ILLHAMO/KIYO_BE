package project.kiyobackend.store.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.kiyobackend.store.adapter.presentation.dto.ImageDto;
import project.kiyobackend.store.domain.domain.store.StoreImage;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserBookmarkResponseDto {

        private Long id;
        private boolean kids;
        private String name;
        private String address;
        private List<ImageDto> images;
        private int reviewCount;
        private int bookmarkCount;
        private boolean isBooked = true;

        public UserBookmarkResponseDto(Long id, boolean kids, List<StoreImage> images, String name, String address, int reviewCount, int bookmarkCount) {
            this.id = id;
            this.kids = kids;
            this.name = name;
            this.address = address;
            this.images = getImagePath(images);
            this.reviewCount = reviewCount;
            this.bookmarkCount = bookmarkCount;

        }

        public List<ImageDto> getImagePath(List<StoreImage> images)
        {
            return images.stream().map(i-> new ImageDto(i.getId(),i.getPath()))
                    .collect(Collectors.toList());
        }

}
