package project.kiyobackend.admin.store.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.kiyobackend.store.presentation.dto.ImageDto;
import project.kiyobackend.store.presentation.dto.TagRequestDto;
import project.kiyobackend.store.presentation.dto.TagResponseDto;
import project.kiyobackend.store.presentation.dto.menu.MenuRequestDto;
import project.kiyobackend.store.presentation.dto.menu.MenuResponseDto;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreDetailRequestForUpdate {

    @Schema(example = "버거킹")
    private String name;
    private boolean kids;
    @Schema(example = "장지역 5분 거리에 있습니다.")
    private String simpleComment;
    private List<TagRequestDto> tag = new ArrayList<>();
    @Schema(example = "송파구 장지동")
    private String address;
    @Schema(example = "09:00~24:00")
    private List<String> time;
    @Schema(example = "장지역 5분 거리에 있습니다.")
    private String detailComment;
    @Schema(example = "서울 송파구 위례중앙로 43")
    private String addressMap;
    private List<Long> convenienceIds;
    private List<Long> categoryIds;
    private List<MenuRequestDto> menuRequests;
}
