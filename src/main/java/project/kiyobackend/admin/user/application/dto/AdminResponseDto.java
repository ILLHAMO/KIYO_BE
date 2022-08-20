package project.kiyobackend.admin.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminResponseDto {

    private Long userSeq;
    private String accessToken;
    private String refreshToken;

}
