package project.kiyobackend.category.presentation;

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
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @GetMapping("/category")
    public List<CategoryResponseDto> getCategories()
    {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(c->
                new CategoryResponseDto(c.getId(),c.getName()))
                .collect(Collectors.toList());
    }

}
