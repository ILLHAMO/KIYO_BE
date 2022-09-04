package project.kiyobackend.admin.store.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import project.kiyobackend.store.domain.domain.tag.Tag;
import project.kiyobackend.store.presentation.dto.ImageDto;
import project.kiyobackend.store.presentation.dto.TagRequestDto;
import project.kiyobackend.store.presentation.dto.TagResponseDto;
import project.kiyobackend.store.presentation.dto.menu.MenuResponseDto;
import project.kiyobackend.store.presentation.dto.review.ReviewResponseDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class StoreDetailResponseForUpdate {

    @Schema(example = "버거킹")
    private String name;
    private boolean kids;
    private boolean isBooked;
    @Schema(example = "장지역 5분 거리에 있습니다.")
    private String simpleComment;
    private List<TagResponseDto> tag = new ArrayList<>();
    @Schema(example = "송파구 장지동")
    private String address;
    @Schema(example = "09:00~24:00")
    private List<String> time;
    @Schema(example = "장지역 5분 거리에 있습니다.")
    private String detailComment;
    @Schema(example = "서울 송파구 위례중앙로 43")
    private String addressMap;
    private List<ImageDto> images;
    private List<Long> convenienceIds;
    private List<MenuResponseDto> menuResponses;
  //  private List<ReviewResponseDto> reviewResponses;

    @Builder
    public StoreDetailResponseForUpdate(String name, boolean kids, boolean isBooked, String simpleComment, List<Tag> tags, String address, List<String> time, String detailComment, String addressMap, List<ImageDto> images, List<Long> convenienceIds, List<MenuResponseDto> menus) {
        this.name = name;
        this.kids = kids;
        this.isBooked = isBooked;
        this.simpleComment = simpleComment;
        this.tag = tags.stream().map(t -> new TagResponseDto(t.getName())).collect(Collectors.toList());
        this.address = address;
        this.time = time;
        this.detailComment = detailComment;
        this.addressMap = addressMap;
        this.images = images;
        this.convenienceIds = convenienceIds;
        this.menuResponses = menus;
     //   this.reviewResponses = reviews;
    }
}
