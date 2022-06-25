package project.kiyobackend.store.adapter.presentation.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuRequestDto {

    private String name;

    private List<MenuOptionRequestDto> menuOptions;


}
