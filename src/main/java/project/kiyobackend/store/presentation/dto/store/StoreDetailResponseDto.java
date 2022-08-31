package project.kiyobackend.store.presentation.dto.store;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.kiyobackend.review.domain.domain.Review;
import project.kiyobackend.store.presentation.dto.ImageDto;
import project.kiyobackend.store.presentation.dto.menu.MenuOptionResponseDto;
import project.kiyobackend.store.presentation.dto.menu.MenuResponseDto;
import project.kiyobackend.store.presentation.dto.review.ReviewImageDto;
import project.kiyobackend.store.presentation.dto.review.ReviewResponseDto;
import project.kiyobackend.store.domain.domain.menu.Menu;
import project.kiyobackend.store.domain.domain.store.StoreImage;
import project.kiyobackend.store.domain.domain.tag.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreDetailResponseDto {
    private String name;
    private boolean kids;
    private boolean isBooked;
    private String simpleComment;
    private List<Tag> tag = new ArrayList<>();
    private String address;
    private List<String> time;
    private String detailComment;
    private String addressMap;
    private List<ImageDto> images;
    private List<Long> convenienceIds;
    private List<MenuResponseDto> menuResponses;
    private List<ReviewResponseDto> reviewResponses;

    @Builder
    public StoreDetailResponseDto(String name, boolean kids, boolean isBooked, String simpleComment, List<Tag> tags, String address, List<String> time, String detailComment, String addressMap, List<StoreImage> images, List<Long> convenienceIds, List<Menu> menus, List<Review> reviews) {
        this.name = name;
        this.kids = kids;
        this.isBooked = isBooked;
        this.simpleComment = simpleComment;
        this.tag = tags;
        this.address = address;
        this.time = time;
        this.detailComment = detailComment;
        this.addressMap = addressMap;
        this.images = images.stream().map(i -> new ImageDto(i.getId(),i.getPath())).collect(Collectors.toList());
        this.convenienceIds = convenienceIds;
        this.menuResponses = menus.stream().map(m-> new MenuResponseDto(m.getId(),m.getName(),m.getMenuOptions().stream().map(mo->new MenuOptionResponseDto(mo.getId(),mo.getName())).collect(Collectors.toList())
        )).collect(Collectors.toList());
        // TODO : UserId => User Nickname으로 변경
        this.reviewResponses = reviews.stream().map(r-> new ReviewResponseDto(r.getId(),r.getUser().getUserId(),r.getUser().getNickname(),r.getUser().getProfileImageUrl(),r.getScore(),r.getContent(),false,
                r.getReviewImages().stream().map(ri -> new ReviewImageDto(ri.getId(),ri.getPath())).collect(Collectors.toList())
        ,r.getLastModifiedDate())).collect(Collectors.toList());
    }
}
