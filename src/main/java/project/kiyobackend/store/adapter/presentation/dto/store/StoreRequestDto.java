package project.kiyobackend.store.adapter.presentation.dto.store;

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

    private String name;
    private String call;
    private boolean kids;
    private String time;
    private String address;
    private Comment comment;
    private List<MenuRequestDto> menus;
    private List<Long> categoryIds;
    private List<Long> convenienceIds;

}
