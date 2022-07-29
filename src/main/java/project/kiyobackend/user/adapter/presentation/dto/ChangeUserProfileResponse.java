package project.kiyobackend.user.adapter.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangeUserProfileResponse {

    private String nickname;
    private String profileImageUrl;
}
