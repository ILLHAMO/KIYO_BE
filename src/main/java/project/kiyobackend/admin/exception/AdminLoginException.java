package project.kiyobackend.admin.exception;

public class AdminLoginException extends RuntimeException{

    public AdminLoginException(String message) {
        super(message);
    }

    public AdminLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public AdminLoginException(Throwable cause) {
        super(cause);
    }

    protected AdminLoginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public AdminLoginException() {
        super();
    }
}
