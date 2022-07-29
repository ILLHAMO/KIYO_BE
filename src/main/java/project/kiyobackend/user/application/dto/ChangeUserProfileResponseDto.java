package project.kiyobackend.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangeUserProfileResponseDto {

    private String nickname;
    private String profileImageUrl;
}
