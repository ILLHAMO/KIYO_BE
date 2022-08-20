package project.kiyobackend.auth.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import project.kiyobackend.auth.token.AuthToken;
import project.kiyobackend.auth.token.AuthTokenProvider;
import project.kiyobackend.common.util.auth.HeaderUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// filter를 통해 서블릿 컨테이너로 들어오기전 http request의 jwt를 파싱해내는 역할을 한다.
@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final AuthTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain)  throws ServletException, IOException {

        System.out.println("TokenAuthentication 호출!");
        // 클라이언트가 보내준 토큰 찾아온다.
        String tokenStr = HeaderUtil.getAccessToken(request);
        if(tokenStr != null)
        {
            // 객체로 변환해준다.
            AuthToken token = tokenProvider.convertAuthToken(tokenStr);


            if (token.validate()) {

                Authentication authentication = tokenProvider.getAuthentication(token);
                // SecurityContextHolder에 값이 존재하는지 여부로 체크
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("토큰 유효성 검사한 후" + authentication.getPrincipal().toString());
            }
        }

        filterChain.doFilter(request, response);
    }

}