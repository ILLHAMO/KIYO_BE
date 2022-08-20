package project.kiyobackend.admin.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.kiyobackend.exception.ApplicationException;
import project.kiyobackend.exception.dto.ApiErrorResponse;

@RestControllerAdvice
public class AdminExceptionHandler {

    @ExceptionHandler(AdminLoginPasswordMismatchException.class)
    public ResponseEntity<ApiErrorResponse> applicationException(ApplicationException e) {

        String errorCode = e.getErrorCode();
        String message = e.getMessage();
        return ResponseEntity.ok(new ApiErrorResponse(errorCode,message));

    }
}
