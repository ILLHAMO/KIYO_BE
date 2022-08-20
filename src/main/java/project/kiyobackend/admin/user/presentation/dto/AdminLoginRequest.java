package project.kiyobackend.admin.user.presentation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminLoginRequest {

    private String adminId;
    private String password;

}
