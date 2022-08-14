package project.kiyobackend.exception.qna;

import org.springframework.http.HttpStatus;
import project.kiyobackend.exception.ApplicationException;

public class NotExistQnAException extends ApplicationException {

    private static final String CODE = "400";
    private static final String MESSAGE = "존재하지 않는 문의사항입니다.";
    public NotExistQnAException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
