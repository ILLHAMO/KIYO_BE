package project.kiyobackend.admin;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.kiyobackend.category.domain.Category;
import project.kiyobackend.category.domain.CategoryRepository;
import project.kiyobackend.category.domain.dto.CategoryRequestDto;

import java.util.Optional;


@RestController
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminCategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping("/admin/categories")
    public String saveCategory(@RequestBody CategoryRequestDto categoryRequestDto)
    {
        categoryRepository.save(new Category(categoryRequestDto.getCategoryName()));
        return "success";
    }


    @DeleteMapping("/admin/categories/{categoryId}")
    public String deleteCategory(@PathVariable Long categoryId)
    {
        Optional<Category> findCategory = categoryRepository.findById(categoryId);
        categoryRepository.delete(findCategory.get());
        return "delete success";
    }

    // TODO : 카테고리 수정, 삭제 기능은 ADMIN에 구현


}
