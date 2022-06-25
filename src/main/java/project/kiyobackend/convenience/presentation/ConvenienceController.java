package project.kiyobackend.convenience.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import project.kiyobackend.convenience.domain.Convenience;
import project.kiyobackend.convenience.domain.ConvenienceRepository;
import project.kiyobackend.convenience.presentation.dto.ConvenienceResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ConvenienceController {

    private final ConvenienceRepository convenienceRepository;

    @GetMapping("/service")
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
