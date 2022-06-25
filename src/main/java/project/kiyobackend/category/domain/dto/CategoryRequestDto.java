package project.kiyobackend.category.domain.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryRequestDto {

    private String categoryName;

    public CategoryRequestDto(String categoryName) {
        this.categoryName = categoryName;
    }
}
