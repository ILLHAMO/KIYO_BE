package project.kiyobackend.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.kiyobackend.category.domain.Category;
import project.kiyobackend.category.domain.CategoryRepository;
import project.kiyobackend.category.domain.dto.CategoryRequestDto;
import project.kiyobackend.category.domain.dto.CategoryResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryRepository categoryRepository;

    @PostMapping("/admin/categories")
    public String saveCategory(@RequestBody CategoryRequestDto categoryRequestDto)
    {
        categoryRepository.save(new Category(categoryRequestDto.getCategoryName()));
        return "success";
    }

    @GetMapping("/categories")
    public List<CategoryResponseDto> showCategories()
    {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                    .map(c->new CategoryResponseDto(c.getId(),c.getName()))
                    .collect(Collectors.toList());
    }

}
