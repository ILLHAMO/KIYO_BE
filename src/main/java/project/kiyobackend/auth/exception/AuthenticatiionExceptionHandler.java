package project.kiyobackend.auth.exception;

import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
public class AuthenticatiionExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ErrorMessage handlerAuthenticationException(AuthenticationException ex)
    {
        ErrorMessage errorMessage = new ErrorMessage(200,new Date(),"not work");
        return errorMessage;
    }
}
