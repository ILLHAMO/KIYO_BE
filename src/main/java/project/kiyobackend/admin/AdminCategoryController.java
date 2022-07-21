package project.kiyobackend.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "CATEGORY ADMIN API",description = "카테고리 관련 어드민 API")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/api/admin")
public class AdminCategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Operation(summary = "카테고리 등록")
    @PostMapping("/category")
    public String saveCategory(@RequestBody CategoryRequestDto categoryRequestDto)
    {
        categoryRepository.save(new Category(categoryRequestDto.getCategoryId(),categoryRequestDto.getCategoryName()));
        return "success";
    }

    @Operation(summary = "카테고리 삭제")
    @DeleteMapping("/category/{categoryId}")
    public String deleteCategory(@PathVariable Long categoryId)
    {
        Optional<Category> findCategory = categoryRepository.findById(categoryId);
        categoryRepository.delete(findCategory.get());
        return "delete success";
    }

    // TODO : 카테고리 수정, 삭제 기능은 ADMIN에 구현


}
