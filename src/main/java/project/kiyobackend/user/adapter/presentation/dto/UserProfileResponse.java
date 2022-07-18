package project.kiyobackend.user.adapter.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileResponse {
    private String userProfileImagePath;
    private String nickname;
}
