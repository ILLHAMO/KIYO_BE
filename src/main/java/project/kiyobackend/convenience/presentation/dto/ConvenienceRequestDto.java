package project.kiyobackend.convenience.presentation.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.kiyobackend.convenience.domain.Convenience;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ConvenienceRequestDto {

    private Long convenienceId;
    private String convenienceName;

}
