package project.kiyobackend.store.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.kiyobackend.store.domain.domain.category.CategoryId;

import javax.persistence.EmbeddedId;

@Data
@AllArgsConstructor
public class CategoryResponseDto {

    private CategoryId id;

    private String categoryName;
}
