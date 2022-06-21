package project.kiyobackend.auth.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
public class AuthTokenProvider {

    private final Key key;
    private static final String AUTHORITIES_KEY = "kiyo";

    public AuthTokenProvider(String secret)
    {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public AuthToken createAuthToken(String id, Date expiry)
    {
        return new AuthToken(id,expiry,key);
    }

    public AuthToken createAuthToken(String id, String role, Date expiry)
    {
        return new AuthToken(id,role,expiry,key);
    }

    // TODO : 역할 정확히 알기
    public AuthToken convertAuthToken(String token)
    {
        return new AuthToken(token,key);
    }

    public Authentication getAuthentication(AuthToken authToken)
    {

        if(authToken.validate())
        {
            Claims claims = authToken.getTokenClaims();

            // 권한들 모두 빼서 SimpleGrantedAuthority로 매핑
            Collection<? extends GrantedAuthority> authorities =
                    Arrays.stream(new String[]{claims.get(AUTHORITIES_KEY).toString()})
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            // 스프링 시큐리티 내부 인증용으로 사용하는 principal 객체
            User principal = new User(claims.getSubject(), "", authorities);

            return new UsernamePasswordAuthenticationToken(principal, authToken, authorities);
        }
        else{
            // TODO : 일단 일반적인 예외로 설정 차후에 TOKEN 관련 예외 커스터마이징
            throw new IllegalStateException();
        }
    }
}
