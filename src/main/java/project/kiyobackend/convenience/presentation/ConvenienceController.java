package project.kiyobackend.convenience.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.kiyobackend.convenience.domain.Convenience;
import project.kiyobackend.convenience.domain.ConvenienceRepository;
import project.kiyobackend.convenience.presentation.dto.ConvenienceResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "CONVENIENCE API",description = "편의 서비스 API")
@RequiredArgsConstructor
@RequestMapping("/api")
public class ConvenienceController {

    private final ConvenienceRepository convenienceRepository;

    @Operation(summary = "편의 서비스 목록 조회")
    @GetMapping("/convenience")
    private List<ConvenienceResponseDto> getConveniences()
    {
        List<Convenience> conveniences = convenienceRepository.findAll();
        return conveniences.
                stream()
                .map(c ->
                        new ConvenienceResponseDto(c.getId(), c.getName()))
                .collect(Collectors.toList());
    }
}
