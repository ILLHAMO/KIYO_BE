package project.kiyobackend.store.adapter.presentation.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MenuResponseDto {

    private Long id;
    private String name;
    private List<MenuOptionResponseDto> menuOptionResponses;
}
