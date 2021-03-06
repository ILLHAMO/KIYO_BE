package project.kiyobackend.category.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryResponseDto {

    private Long id;
    private String categoryName;
}
