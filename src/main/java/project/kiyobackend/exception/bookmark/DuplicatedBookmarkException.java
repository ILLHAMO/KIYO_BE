package project.kiyobackend.exception.bookmark;


import org.springframework.http.HttpStatus;
import project.kiyobackend.exception.ApplicationException;

public class DuplicatedBookmarkException extends ApplicationException {

    private static final String CODE = "400";
    private static final String MESSAGE = "이미 북마크한 게시물 중복 북마크 에러";


    public DuplicatedBookmarkException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}