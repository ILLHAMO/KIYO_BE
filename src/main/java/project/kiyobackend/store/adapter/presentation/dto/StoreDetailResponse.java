package project.kiyobackend.store.adapter.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import project.kiyobackend.review.domain.domain.Review;
import project.kiyobackend.store.domain.domain.menu.Menu;
import project.kiyobackend.store.domain.domain.store.StoreImage;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class StoreDetailResponse {

    private String name;
    private boolean kids;
    private boolean isBooked;
    private String simpleComment;
    private String tag;
    private String address;
    private String time;
    private String detailComment;
    private String addressMap;
    private List<ImageDto> images;
    private List<ConvenienceResponseDto> convenienceResponses;
    private List<MenuResponseDto> menuResponses;
    private List<ReviewResponseDto> reviewResponses;

    @Builder
    public StoreDetailResponse(String name, boolean kids, boolean isBooked, String simpleComment, String tag, String address, String time, String detailComment, String addressMap, List<StoreImage> images, List<Long> convenienceIds, List<Menu> menus, List<Review> reviews) {
        this.name = name;
        this.kids = kids;
        this.isBooked = isBooked;
        this.simpleComment = simpleComment;
        this.tag = tag;
        this.address = address;
        this.time = time;
        this.detailComment = detailComment;
        this.addressMap = addressMap;
        this.images = images.stream().map(i -> new ImageDto(i.getId(),i.getPath())).collect(Collectors.toList());
        this.menuResponses = menus.stream().map(m-> new MenuResponseDto(m.getId(),m.getName(),m.getMenuOptions().stream().map(mo->new MenuOptionResponseDto(mo.getId(),mo.getName())).collect(Collectors.toList())
                )).collect(Collectors.toList());
        this.reviewResponses = reviews.stream().map(r-> new ReviewResponseDto(r.getId(),r.getUser().getNickname(),r.getScore(),
                r.getReviewImages().stream().map(ri -> new ReviewImageDto(ri.getId(),ri.getPath())).collect(Collectors.toList())
                )).collect(Collectors.toList());
    }
}