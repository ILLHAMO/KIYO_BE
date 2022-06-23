package project.kiyobackend.store.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.kiyobackend.category.domain.CategoryId;

@Data
@AllArgsConstructor
public class CategoryResponseDto {

    private CategoryId id;

    private String categoryName;
}
