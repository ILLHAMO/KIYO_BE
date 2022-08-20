package project.kiyobackend.admin.exception;

public class AdminLoginPasswordMismatchException extends RuntimeException{

    public AdminLoginPasswordMismatchException(String message) {
        super(message);
    }

    public AdminLoginPasswordMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public AdminLoginPasswordMismatchException(Throwable cause) {
        super(cause);
    }

    protected AdminLoginPasswordMismatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public AdminLoginPasswordMismatchException() {
        super();
    }
}
