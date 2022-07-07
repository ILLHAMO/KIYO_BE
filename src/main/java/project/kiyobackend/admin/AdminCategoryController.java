package project.kiyobackend.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.kiyobackend.category.domain.Category;
import project.kiyobackend.category.domain.CategoryRepository;
import project.kiyobackend.category.domain.dto.CategoryRequestDto;

import java.util.Optional;


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

    // 카테고리 삭제
    @DeleteMapping("/admin/categories/{categoryId}")
    public String deleteCategory(@PathVariable Long categoryId)
    {
        Optional<Category> findCategory = categoryRepository.findById(categoryId);
        categoryRepository.delete(findCategory.get());
        return "delete success";
    }

    // TODO : 카테고리 수정, 삭제 기능은 ADMIN에 구현


}
