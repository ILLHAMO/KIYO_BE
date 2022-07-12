package project.kiyobackend.exception.dto;

public class ApiErrorResponse {

    private String errorCode;
    private String message;

    private ApiErrorResponse() {
    }

    public ApiErrorResponse(String errorCode,String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
