package project.kiyobackend.store.adapter.in.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.kiyobackend.store.adapter.in.web.dto.CategoryResponseDto;
import project.kiyobackend.store.adapter.in.web.dto.ConvenienceResponseDto;
import project.kiyobackend.store.domain.domain.category.Category;
import project.kiyobackend.store.domain.domain.category.CategoryRepository;
import project.kiyobackend.store.domain.domain.convenience.Convenience;
import project.kiyobackend.store.domain.domain.convenience.ConvenienceRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StoreController{

    private final CategoryRepository categoryRepository;
    private final ConvenienceRepository convenienceRepository;

    @GetMapping("/categories")
    public List<CategoryResponseDto> categories()
    {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                        .map(c-> new CategoryResponseDto(c.getId(),c.getName()))
                        .collect(Collectors.toList());
    }

    @GetMapping("/service")
    public List<ConvenienceResponseDto> conveniences()
    {
        List<Convenience> conveniences = convenienceRepository.findAll();
        return conveniences.stream()
                            .map(cv -> new ConvenienceResponseDto(cv.getId(),cv.getName()))
                            .collect(Collectors.toList());
    }
}
