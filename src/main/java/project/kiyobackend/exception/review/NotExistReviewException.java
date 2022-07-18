package project.kiyobackend.exception.review;

import org.springframework.http.HttpStatus;
import project.kiyobackend.exception.ApplicationException;

public class NotExistReviewException extends ApplicationException {

    private static final String CODE = "400";
    private static final String MESSAGE = "존재하지 않는 리뷰입니다.";
    public NotExistReviewException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
