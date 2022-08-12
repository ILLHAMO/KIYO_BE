package project.kiyobackend.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileResponseDto {
    private String profileImagePath;
    private String nickname;
}
