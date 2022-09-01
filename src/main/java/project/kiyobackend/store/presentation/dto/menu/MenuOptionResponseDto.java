package project.kiyobackend.store.presentation.dto.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MenuOptionResponseDto {

      private Long id;
      @Schema(example = "와퍼 세트")
      private String name;
}
