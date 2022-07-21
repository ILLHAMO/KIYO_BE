package project.kiyobackend.category.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.kiyobackend.category.domain.Category;
import project.kiyobackend.category.domain.CategoryRepository;
import project.kiyobackend.category.domain.dto.CategoryResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "CATEGORY API",description = "카테고리 API")
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @Operation(summary = "카테고리 목록 조회")
    @GetMapping("/category")
    public List<CategoryResponseDto> getCategories()
    {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(c->
                new CategoryResponseDto(c.getId(),c.getName()))
                .collect(Collectors.toList());
    }

}
