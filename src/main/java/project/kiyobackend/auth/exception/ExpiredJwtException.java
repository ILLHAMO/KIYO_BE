package project.kiyobackend.auth.exception;

public class ExpiredJwtException extends RuntimeException{

    public ExpiredJwtException()
    {
        super("리프레시 토큰 필요");
    }

    public ExpiredJwtException(String message)
    {
        super(message);
    }
}
