package project.kiyobackend.store.adapter.presentation.dto.store;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.kiyobackend.store.adapter.presentation.dto.menu.MenuRequestDto;
import project.kiyobackend.store.domain.domain.store.Comment;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreRequestDto implements Serializable {

    @Schema(example = "이모네 생고기")
    private String name;
    @Schema(example = "02-4334-4843")
    private String call;
    @Schema(example = "false")
    private boolean kids;
  //  private List<String> time;
    @Schema(example = "서울시 성동구")
    private String address;
  //  private String addressMap;
 //   private Comment comment;
 //   private List<MenuRequestDto> menus;
    private List<Long> categoryIds;
    private List<Long> convenienceIds;

}
