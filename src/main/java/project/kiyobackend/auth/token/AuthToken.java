package project.kiyobackend.auth.token;

import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.kiyobackend.auth.common.ApiResponse;

import java.security.Key;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Getter
public class AuthToken {

    private final String token;
    private final Key key;

    private static final String AUTHORITIES_KEY = "kiyo";

    AuthToken(String id, Date expiry, Key key)
    {
        this.key = key;
        this.token = createAuthToken(id,expiry);
    }

    AuthToken(String id,String role, Date expiry, Key key)
    {
        this.key = key;
        this.token = createAuthToken(id,role,expiry);
    }

    private String createAuthToken(String id, Date expiry) {
        return Jwts.builder()
                .setSubject(id)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiry)
                .compact();
    }

    // TODO : createAuthToken 메서드 분리 이유 분석하기
    private String createAuthToken(String id, String role, Date expiry) {
        return Jwts.builder()
                .setSubject(id)
                .claim(AUTHORITIES_KEY, role)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiry)
                .compact();
    }

    public boolean validate() {
        return this.getTokenClaims() != null;
    }

    public Claims getTokenClaims() {
        try {
            // 만약 아무 이상이 없다면 여기서 Claims 반환하고 끝나야함!
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException e) {
            log.info("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }
        return null;
    }

    /**
     * 여기서는 ExpiredJwtException이 발생해야만 claim 추출 가능하다!
     */
    public Claims getExpiredTokenClaims() {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.info("Expired JWT token claim : " + e.getClaims());
            return e.getClaims();
        }
        return null;
    }

}
