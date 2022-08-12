package project.kiyobackend.exception.bookmark;

import org.springframework.http.HttpStatus;
import project.kiyobackend.exception.ApplicationException;

public class NotExistBookmarkException extends ApplicationException {

    private static final String CODE = "400";
    private static final String MESSAGE = "";
    public NotExistBookmarkException() {
        super(CODE,HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
