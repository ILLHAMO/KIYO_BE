package project.kiyobackend.exception.user;

import org.springframework.http.HttpStatus;
import project.kiyobackend.exception.ApplicationException;

public class NotExistUserException extends ApplicationException {


    private static final String CODE = "400";
    private static final String MESSAGE = "존재하지 않는 유저 정보입니다.";
    public NotExistUserException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
