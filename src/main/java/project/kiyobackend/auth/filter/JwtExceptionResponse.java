package project.kiyobackend.auth.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class JwtExceptionResponse {

    private String message;
    private HttpStatus httpStatus;
}
