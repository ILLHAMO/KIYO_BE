package project.kiyobackend.auth.web;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import project.kiyobackend.auth.common.LogoutDto;
import project.kiyobackend.auth.common.RefreshTokenInvalidDto;
import project.kiyobackend.auth.common.TokenNotExpiredDto;
import project.kiyobackend.auth.common.TokenSucessDto;
import project.kiyobackend.auth.config.properties.AppProperties;

import project.kiyobackend.auth.entity.RoleType;

import project.kiyobackend.auth.repository.UserRefreshTokenRepository;
import project.kiyobackend.auth.token.AuthToken;
import project.kiyobackend.auth.token.AuthTokenProvider;
import project.kiyobackend.auth.token.UserRefreshToken;
import project.kiyobackend.user.domain.User;
import project.kiyobackend.user.domain.UserRepository;
import project.kiyobackend.util.auth.CookieUtil;
import project.kiyobackend.util.auth.HeaderUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final UserRepository userRepository;
    private final static long THREE_DAYS_MSEC = 259200000;
    private final static String REFRESH_TOKEN = "refresh_token";

    @DeleteMapping("/logout")
    public ResponseEntity<LogoutDto> logout(HttpServletRequest request, HttpServletResponse response)
    {
        // 쿠키에 지금 들어있는 거 빼와서
        String refreshToken = CookieUtil.getCookie(request, REFRESH_TOKEN)
                .map(Cookie::getValue)
                .orElse((null));
        // 저장소에서 날려버리고
        Optional<UserRefreshToken> userRefreshToken = userRefreshTokenRepository.findByRefreshToken(refreshToken);
        userRefreshTokenRepository.delete(userRefreshToken.get());

        // 쿠키 날려버리고
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
        return ResponseEntity.ok(new LogoutDto(true,"로그아웃 성공"));
    }


    @GetMapping("/refresh")
    public ResponseEntity<?> refreshToken (HttpServletRequest request, HttpServletResponse response) {

        /**
         * access token을 Header의 Authorization에서 파싱
         */
        String accessToken = HeaderUtil.getAccessToken(request);
        System.out.println("accessToken : " + accessToken);
        // 만약 accessToken이 아예 없다면?

        if(accessToken.length() < 6)
        {

                String refreshToken = CookieUtil.getCookie(request, REFRESH_TOKEN)
                        .map(Cookie::getValue)
                        .orElse((null));
                Optional<UserRefreshToken> byRefreshToken = userRefreshTokenRepository.findByRefreshToken(refreshToken);
                if(byRefreshToken.isEmpty())
                {
                    return ResponseEntity.ok(new RefreshTokenInvalidDto(401,false,"재로그인 필요"));
                }
                String userId = byRefreshToken.get().getUserId();
                User byUserId = userRepository.findByUserId(userId);
                System.out.println("userId : " + byUserId.getUserId());
                RoleType roleType = byUserId.getRoleType();
                Date now = new Date();
                AuthToken newAccessToken = tokenProvider.createAuthToken(
                        userId,
                        roleType.getCode(),
                        new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())


            );

            return ResponseEntity.ok(new TokenSucessDto(200,true,"token", newAccessToken.getToken()));
        }
        else
        {
            AuthToken authToken = tokenProvider.convertAuthToken(accessToken);


            Claims claims = authToken.getExpiredTokenClaims();

            if (claims == null) {
                return ResponseEntity.ok(new TokenNotExpiredDto(401,false,"엑세스 토큰이 만료되지 않았습니다")); }

            // claims으로부터 userId 빼냄
            // 지금 작업하고 있는 claims는 만료된 access token의 claims이다
            String userId = claims.getSubject();
            System.out.println("userId : " + userId);
            RoleType roleType = RoleType.of(claims.get("role", String.class));
            System.out.println("roletype : " + roleType);

        /*
        쿠키로부터 refreshToken 파싱해낸다.
         */
            String refreshToken = CookieUtil.getCookie(request, REFRESH_TOKEN)
                    .map(Cookie::getValue)
                    .orElse((null));

            // 지금 쿠키에 있던 refreshtoken 빼내기
            AuthToken authRefreshToken = tokenProvider.convertAuthToken(refreshToken);

            // 토큰 유효성 검사 : 쿠키의 refreshtoken 이상하면 다시 로그인
            // 이상하면 로그아웃 시키기
            if (!authRefreshToken.validate()) {
                return ResponseEntity.ok(new RefreshTokenInvalidDto(401,false,"재로그인 필요"));// invalid refreshToken 나오면 로그아웃
            }


            UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserIdAndRefreshToken(userId, refreshToken);

            // 찾았는데 없다? 로그아웃
            if (userRefreshToken == null) {
                return ResponseEntity.ok(new RefreshTokenInvalidDto(401,false,"재로그인 필요"));
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
                userRefreshTokenRepository.saveAndFlush(userRefreshToken);

                int cookieMaxAge = (int) refreshTokenExpiry / 60;
            /*
            현재 들어있는 리프레쉬 토큰 날려버리고, 새로운 리프레쉬 토큰 쿠키에 넣어준다!
             */
                CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
                CookieUtil.addCookie(response, REFRESH_TOKEN, authRefreshToken.getToken(), cookieMaxAge);
            }

            // 성공했을때!
            return ResponseEntity.ok(new TokenSucessDto(200,true,"token", newAccessToken.getToken()));
        }

    }


}