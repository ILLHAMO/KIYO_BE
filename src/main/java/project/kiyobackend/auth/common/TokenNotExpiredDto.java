package project.kiyobackend.auth.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TokenNotExpiredDto {
    private int code;
    private boolean success;
    private String message;
}
