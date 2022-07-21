package project.kiyobackend.user.adapter.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileResponse {
    @Schema(description = "유저 프로필 사진", example = "https://kiyo.s3.amazonaws.com/12")
    private String userProfileImagePath;
    @Schema(description = "유저 닉네임", example = "kiyo")
    private String nickname;
}
