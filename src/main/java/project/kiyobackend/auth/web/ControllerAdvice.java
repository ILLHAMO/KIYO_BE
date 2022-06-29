package project.kiyobackend.auth.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import project.kiyobackend.auth.exception.ExpiredJwtException;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(ExpiredJwtException.class)
    private ResponseEntity<ErrorResponse> handleRefreshException(ExpiredJwtException e)
    {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.OK);
    }
}
