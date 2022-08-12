package project.kiyobackend.exception.store;

import org.springframework.http.HttpStatus;
import project.kiyobackend.exception.ApplicationException;

public class NotExistStoreException extends ApplicationException {

    private static final String CODE = "400";
    private static final String MESSAGE = "존재하지 않는 가게 정보입니다.";
    public NotExistStoreException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
