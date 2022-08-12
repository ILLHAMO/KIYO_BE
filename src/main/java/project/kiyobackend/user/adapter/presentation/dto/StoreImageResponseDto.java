package project.kiyobackend.user.adapter.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StoreImageResponseDto {

    private Long id;
    private String imagePath;
}
