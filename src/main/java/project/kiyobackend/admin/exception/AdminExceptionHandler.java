package project.kiyobackend.admin.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.kiyobackend.exception.dto.ApiErrorResponse;

@RestControllerAdvice
public class AdminExceptionHandler {

    @ExceptionHandler(AdminLoginException.class)
    public ResponseEntity<ApiErrorResponse> applicationException(RuntimeException e) {

        System.out.println("호출!" + e.getMessage());
        return ResponseEntity.ok(new ApiErrorResponse("401",e.getMessage()));

    }
}
