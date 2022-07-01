package project.kiyobackend.auth.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import project.kiyobackend.auth.exception.TokenValidFailedException;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
public class AuthTokenProvider {

    private final Key key;
    private static final String AUTHORITIES_KEY = "role";

    public AuthTokenProvider(String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public AuthToken createAuthToken(String id, Date expiry) {
        return new AuthToken(id, expiry, key);
    }

    public AuthToken createAuthToken(String id, String role, Date expiry) {
        return new AuthToken(id, role, expiry, key);
    }

    public AuthToken convertAuthToken(String token) {
        return new AuthToken(token, key);
    }

    public Authentication getAuthentication(AuthToken authToken) {

            // 앞에서 유효성 검사 한번 했기 때문에 따로 안해도 된다.
            // UserId와 권한 정보 들어있는 claims 빼오는 로직
            Claims claims = authToken.getTokenClaims();

            // 권한 정보 빼오는 로직
            Collection<? extends GrantedAuthority> authorities =
                    Arrays.stream(new String[]{claims.get(AUTHORITIES_KEY).toString()})
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

            // claims의 subject에 userId가 들어있다.
            log.debug("claims subject := [{}]", claims.getSubject());

            // 유저 id와 권한 정보로 UserDetails 생성
            User principal = new User(claims.getSubject(), "", authorities);

            // 결과물로 Authentication 만들어냄
            return new UsernamePasswordAuthenticationToken(principal, authToken, authorities);

    }

}