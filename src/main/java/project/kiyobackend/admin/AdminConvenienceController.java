package project.kiyobackend.admin;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.kiyobackend.category.domain.Category;
import project.kiyobackend.category.domain.CategoryRepository;
import project.kiyobackend.category.domain.dto.CategoryRequestDto;
import project.kiyobackend.convenience.domain.Convenience;
import project.kiyobackend.convenience.domain.ConvenienceRepository;
import project.kiyobackend.convenience.presentation.dto.ConvenienceRequestDto;

import java.util.Optional;

@RestController
@Tag(name = "CONVENIENCE ADMIN API",description = "편의 서비스 관련 어드민 API")
@RequestMapping("/api/admin")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminConvenienceController {

    @Autowired
    private ConvenienceRepository convenienceRepository;

    @Operation(summary = "편의 서비스 등록")
    @PostMapping("/convenience")
    public String saveConvenience(@RequestBody ConvenienceRequestDto convenienceRequestDto)
    {
        convenienceRepository.save(new Convenience(
                convenienceRequestDto.getConvenienceId(),
                convenienceRequestDto.getConvenienceName()));
        return "success";
    }

    @Operation(summary = "편의 서비스 삭제")
    @DeleteMapping("/convenience/{convenienceId}")
    public String deleteConvenience(@PathVariable Long convenienceId)
    {
        Optional<Convenience> findConveneince = convenienceRepository.findById(convenienceId);
        convenienceRepository.delete(findConveneince.get());
        return "delete success";
    }

    // TODO : 카테고리 수정, 삭제 기능은 ADMIN에 구현


}