package project.kiyobackend.auth.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LogoutDto {
    private boolean success;
    private String message;
}
