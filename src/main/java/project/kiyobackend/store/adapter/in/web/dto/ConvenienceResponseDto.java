package project.kiyobackend.store.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.kiyobackend.convenience.domain.ConvenienceId;

@AllArgsConstructor
@Data
public class ConvenienceResponseDto {

    private ConvenienceId id;

    private String serviceName;
}
