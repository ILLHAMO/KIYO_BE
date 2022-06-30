package project.kiyobackend.auth.web;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import project.kiyobackend.auth.common.ApiResponse;
import project.kiyobackend.auth.config.properties.AppProperties;
import project.kiyobackend.auth.entity.AuthReqModel;
import project.kiyobackend.auth.entity.RoleType;
import project.kiyobackend.auth.entity.UserPrincipal;
import project.kiyobackend.auth.repository.UserRefreshTokenRepository;
import project.kiyobackend.auth.token.AuthToken;
import project.kiyobackend.auth.token.AuthTokenProvider;
import project.kiyobackend.auth.token.UserRefreshToken;
import project.kiyobackend.util.auth.CookieUtil;
import project.kiyobackend.util.auth.HeaderUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private final UserRefreshTokenRepository userRefreshTokenRepository;

    private final static long THREE_DAYS_MSEC = 259200000;
    private final static String REFRESH_TOKEN = "refresh_token";


    @GetMapping("/refresh")
    public ApiResponse refreshToken (HttpServletRequest request, HttpServletResponse response) {

        /**
         * access token을 Header의 Authorization에서 파싱
         */
        String accessToken = HeaderUtil.getAccessToken(request);

        /**
         * AuthToken 객체로 변환
         */
        AuthToken authToken = tokenProvider.convertAuthToken(accessToken);

        /**
         * 정상 토큰인지 아닌지 판별,
         * 만약 refresh로 들어왔는데 validate 하지 않으면, invalidAccessToken이라고 판별
         */
        if (!authToken.validate()) {
            return ApiResponse.invalidAccessToken();
        }

        /**
         * 여기서는 null이 나오면 아무 문제 없는 토큰이기 때문에 문제 발생한다.
         */
        Claims claims = authToken.getExpiredTokenClaims();
        if (claims == null) {
            return ApiResponse.notExpiredTokenYet();
        }

        // claims으로부터 userId 빼냄
        String userId = claims.getSubject();
        RoleType roleType = RoleType.of(claims.get("role", String.class));

        /*
        쿠키로부터 refreshToken 파싱해낸다.
         */
        String refreshToken = CookieUtil.getCookie(request, REFRESH_TOKEN)
                .map(Cookie::getValue)
                .orElse((null));

        AuthToken authRefreshToken = tokenProvider.convertAuthToken(refreshToken);

        /*
        refreshToken이 올바른지 체크
         */
        if (!authRefreshToken.validate()) {
            return ApiResponse.invalidRefreshToken();
        }

        /*
         userId refresh token 으로 DB 확인
         즉, 쿠키에 들어있는 refresh Token과 내가 DB에 보관하고 있는 refreshToken이 동일한 것인지 체크한다.
         */
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserIdAndRefreshToken(userId, refreshToken);
        if (userRefreshToken == null) {
            return ApiResponse.invalidRefreshToken();
        }

        /**
         * 여기부터 refreshToken 정상 인증 되었을때 상황, 새로운 accessToken 생성!
         */
        Date now = new Date();
        AuthToken newAccessToken = tokenProvider.createAuthToken(
                userId,
                roleType.getCode(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        long validTime = authRefreshToken.getTokenClaims().getExpiration().getTime() - now.getTime();

        // refresh 토큰 기간이 3일 이하로 남은 경우, refresh 토큰 갱신
        // refresh 토큰 기간도 주기적으로 업데이트 해줘야 한다!
        if (validTime <= THREE_DAYS_MSEC) {
            // refresh 토큰 설정
            long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

            authRefreshToken = tokenProvider.createAuthToken(
                    appProperties.getAuth().getTokenSecret(),
                    new Date(now.getTime() + refreshTokenExpiry)
            );

            // DB에 refresh 토큰 업데이트
            // durty checking 가능함으로 값만 변경!
            userRefreshToken.setRefreshToken(authRefreshToken.getToken());

            int cookieMaxAge = (int) refreshTokenExpiry / 60;
            /*
            현재 들어있는 리프레쉬 토큰 날려버리고, 새로운 리프레쉬 토큰 쿠키에 넣어준다!
             */
            CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
            CookieUtil.addCookie(response, REFRESH_TOKEN, authRefreshToken.getToken(), cookieMaxAge);
        }

        return ApiResponse.success("token", newAccessToken.getToken());
    }
}