package project.kiyobackend.admin.user.presentation;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LogoutRequestDto {
    private String refreshToken;
}
