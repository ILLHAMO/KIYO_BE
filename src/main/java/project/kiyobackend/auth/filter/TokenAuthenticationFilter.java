package project.kiyobackend.auth.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
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
    private final RedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain)  throws ServletException, IOException {

        System.out.println("TokenAuthentication 호출!");
        // 클라이언트가 보내준 토큰 찾아온다.
        String tokenStr = HeaderUtil.getAccessToken(request);

        // 1. 만약에 토큰이 없다면 -> 그냥 넘어감
        if(tokenStr != null)
        {
            System.out.println("여기까지 들어옴");
            // 토큰은 있는데
            if(redisTemplate.opsForValue().get(tokenStr) == null)
            {
                System.out.println("레디스 검사");
                AuthToken token = tokenProvider.convertAuthToken(tokenStr);
                if (token.validate()) {
                    Authentication authentication = tokenProvider.getAuthentication(token);
                    // SecurityContextHolder에 값이 존재하는지 여부로 체크
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("토큰 유효성 검사한 후" + authentication.getPrincipal().toString());
                }
            }
            else{
                System.out.println("로그아웃된 액세스 토큰입니다!");
            }
        }

        filterChain.doFilter(request, response);
    }

}