package project.kiyobackend.auth.web;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
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
import project.kiyobackend.auth.web.dto.ExpiredJwtTokenDto;
import project.kiyobackend.auth.web.dto.LoginRequestDto;
import project.kiyobackend.auth.web.dto.LoginResponseDto;
import project.kiyobackend.exception.user.NotExistUserException;
import project.kiyobackend.user.domain.User;
import project.kiyobackend.user.domain.UserRepository;
import project.kiyobackend.common.util.auth.CookieUtil;
import project.kiyobackend.common.util.auth.HeaderUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Optional;

@RestController
@Tag(name = "AUTH API",description = "인증 관련 API")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final UserRepository userRepository;
    private final static long THREE_DAYS_MSEC = 259200000;
    private final static String REFRESH_TOKEN = "refresh_token";

    @Operation(summary = "사용자 로그아웃")
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/logout")
    public ResponseEntity<LogoutDto> logout(HttpServletRequest request, HttpServletResponse response)
    {
        String refreshToken = CookieUtil.getCookie(request, REFRESH_TOKEN)
                .map(Cookie::getValue)
                .orElse((null));

        System.out.println("로그아웃 시에 쿠키에 refreshToken 잘 들어있는지 체크 : " + refreshToken);

        Optional<UserRefreshToken> userRefreshToken = userRefreshTokenRepository.findByRefreshToken(refreshToken);
        if(userRefreshToken.isPresent())
        {
            userRefreshTokenRepository.delete(userRefreshToken.get());
        }

        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
        return ResponseEntity.ok(new LogoutDto(true,"로그아웃 성공"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto)
    {
        User user = userRepository.findByUserId(loginRequestDto.getUserId()).orElseThrow(NotExistUserException::new);
                Date now = new Date();
        AuthToken newAccessToken = tokenProvider.createAuthToken(
                user.getUserId(),
                user.getRoleType().getCode(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );
        return ResponseEntity.ok(new LoginResponseDto(newAccessToken.getToken()));
    }

    @Operation(summary = "리프레시 토큰 재발급")
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken (HttpServletRequest request, HttpServletResponse response, @RequestBody ExpiredJwtTokenDto expiredJwtTokenDto) {

        // Refresh 요청이 들어오는 경우는 2가지가 있음
        //1. 엑세스 토큰 기간 다 만료됐을때
        //2. 페이지 새로고침 되거나 브라우저가 꺼져서 refreshToken은 있지만 accessToken이 없을때
      //  String accessToken = HeaderUtil.getAccessToken(request);
        String accessToken = expiredJwtTokenDto.getExpiredToken();
        System.out.println("Refresh 요청으로 들어온 accessToken : " + accessToken);


        //Case 2. 엑세스 토큰 기한 만료로 들어왔을때
            AuthToken authToken = tokenProvider.convertAuthToken(accessToken);

            Claims claims = authToken.getExpiredTokenClaims();

            if (claims == null) {
                return ResponseEntity.ok(new TokenNotExpiredDto(401,false,"엑세스 토큰이 만료되지 않았습니다")); }

            //2-1. 토큰을 만들 정보 빼냄
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
        System.out.println("쿠키에서 파싱해낸 refreshToken : " + refreshToken);
            // 지금 쿠키에 있던 refreshtoken 빼내기
            AuthToken authRefreshToken = tokenProvider.convertAuthToken(refreshToken);
            // 토큰 유효성 검사 : 쿠키의 refreshtoken 이상하면 다시 로그인
            // 이상하면 로그아웃 시키기
            if (!authRefreshToken.validate()) {

                CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);

                Optional<UserRefreshToken> userRefreshToken = userRefreshTokenRepository.findByRefreshToken(refreshToken);
                if(userRefreshToken.isPresent())
                {
                    userRefreshTokenRepository.delete(userRefreshToken.get());

                }
                return ResponseEntity.ok(new RefreshTokenInvalidDto(401,false,"재로그인 필요"));// invalid refreshToken 나오면 로그아웃
            }


            UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserIdAndRefreshToken(userId, refreshToken);

            // 찾았는데 없다? 로그아웃
            if (userRefreshToken == null) {

                CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
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