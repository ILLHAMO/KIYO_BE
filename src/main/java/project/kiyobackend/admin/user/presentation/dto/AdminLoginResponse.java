package project.kiyobackend.admin.user.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminLoginResponse {
    private String accessToken;
    private String refreshToken;
}
