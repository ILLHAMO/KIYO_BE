package project.kiyobackend.auth.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


// 만약 인증에 문제 생기면 여기로 온다.
@Component
@RequiredArgsConstructor
public class TokenAccessDeniedHandler implements AccessDeniedHandler {

    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        // 예외가 외부로 전파되도록 놔두지 않고 정상 흐름으로 바꿔주는 역할을 하는게 exceptionResolver다.
        System.out.println("TokenAccessDeniedHandler#handle 호출");
        handlerExceptionResolver.resolveException(request, response, null, accessDeniedException);
    }
}