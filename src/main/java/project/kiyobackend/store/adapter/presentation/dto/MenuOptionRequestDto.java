package project.kiyobackend.store.adapter.presentation.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuOptionRequestDto {

    private String name;
}
