package project.kiyobackend.category.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryResponseDto {

    private Long id;
    private String categoryName;
}
