package project.kiyobackend.store.presentation.dto.menu;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuOptionRequestDto {

    private String name;
}
