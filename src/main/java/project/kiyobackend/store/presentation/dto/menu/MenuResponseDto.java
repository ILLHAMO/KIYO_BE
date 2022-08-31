package project.kiyobackend.store.presentation.dto.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MenuResponseDto {

    private Long id;
    @Schema(example = "식사류")
    private String name;
    private List<MenuOptionResponseDto> menuOptionResponses;
}
