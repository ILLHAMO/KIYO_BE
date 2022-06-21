package project.kiyobackend.store.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.kiyobackend.store.domain.domain.convenience.ConvenienceId;

import javax.persistence.EmbeddedId;

@AllArgsConstructor
@Data
public class ConvenienceResponseDto {

    private ConvenienceId id;

    private String serviceName;
}
