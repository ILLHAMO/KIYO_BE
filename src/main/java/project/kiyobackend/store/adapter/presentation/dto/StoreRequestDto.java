package project.kiyobackend.store.adapter.presentation.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.kiyobackend.store.domain.domain.store.Comment;

import java.security.ProtectionDomain;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreRequestDto {

    private String name;
    private String call;
    private boolean kids;
    private String time;
    private Comment comment;
    private List<MenuRequestDto> menus;
    private List<String> images;
    private List<Long> categoryIds;
    private List<Long> convenienceIds;


}
