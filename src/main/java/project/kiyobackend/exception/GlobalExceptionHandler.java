package project.kiyobackend.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.kiyobackend.exception.dto.ApiErrorResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String INTERNAL_SERVER_ERROR_CODE = "500";

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiErrorResponse> applicationException(ApplicationException e) {
        String errorCode = e.getErrorCode();
        String message = e.getMessage();
        log.warn(
                e.getClass().getSimpleName(),
                errorCode,
                e.getMessage()
        );
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ApiErrorResponse(errorCode,message));
    }


}
