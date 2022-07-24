package project.kiyobackend.auth.web;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

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
    @GetMapping("/refresh")
    public ResponseEntity<?> refreshToken (HttpServletRequest request, HttpServletResponse response) {

        // Refresh 요청이 들어오는 경우는 2가지가 있음
        //1. 엑세스 토큰 기간 다 만료됐을때
        //2. 페이지 새로고침 되거나 브라우저가 꺼져서 refreshToken은 있지만 accessToken이 없을때
        String accessToken = HeaderUtil.getAccessToken(request);
        System.out.println("refresh로 들어온 refreshToken이 : " + accessToken);
        // Case 1. accessToken이 없을때, 즉 브라우저가 새로 고침될때
        if(accessToken == null)
        {
            //1-1. 쿠키에서 refreshToken 파싱
            String refreshToken = CookieUtil.getCookie(request, REFRESH_TOKEN)
                    .map(Cookie::getValue)
                    .orElse((null));

            System.out.println("refeshtoken이 들어있는지 잘봐 : " + refreshToken);
            //1-2. 토큰 객체로 변환
            AuthToken authRefreshToken = tokenProvider.convertAuthToken(refreshToken);

            // 만약 refreshtoken이 정상이 아니라면 재 로그인 필요
            if (!authRefreshToken.validate()) {
                return ResponseEntity.ok(new RefreshTokenInvalidDto(401,false,"재로그인 필요"));// invalid refreshToken 나오면 로그아웃
            }

            //1-2. 쿠키에 들어있던 refreshToken과 실제 DB의 refreshToken이 같은지 판별
            Optional<UserRefreshToken> byRefreshToken = userRefreshTokenRepository.findByRefreshToken(refreshToken);

            //1-3. 만약 DB내에 리프레시 토큰이 없다면 재로그인이 필요하다.
            if(byRefreshToken.isEmpty())
            {
                return ResponseEntity.ok(new RefreshTokenInvalidDto(401,false,"재로그인 필요"));
            }

            //1-4. refreshToken 엔티티 내부에는 userId 존재
            //1-5. 지금 토큰 발행하려는 사람의 정보 얻어옴
            String userId = byRefreshToken.get().getUserId();

            User byUserId = userRepository.findByUserId(userId).orElseThrow(NotExistUserException::new);

            System.out.println("userId : " + byUserId.getUserId());

            RoleType roleType = byUserId.getRoleType();

            //2. 새로운 accessToken 만들어냄
            Date now = new Date();
            AuthToken newAccessToken = tokenProvider.createAuthToken(
                    userId,
                    roleType.getCode(),
                    new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())

            );

            long validTime = authRefreshToken.getTokenClaims().getExpiration().getTime() - now.getTime();

            if (validTime <= THREE_DAYS_MSEC) {
                // refresh 토큰 설정
                long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

                authRefreshToken = tokenProvider.createAuthToken(
                        appProperties.getAuth().getTokenSecret(),
                        new Date(now.getTime() + refreshTokenExpiry)
                );

                // DB에 refresh 토큰 업데이트
                // durty checking 가능함으로 값만 변경!
                byRefreshToken.get().setRefreshToken(authRefreshToken.getToken());
                userRefreshTokenRepository.saveAndFlush(byRefreshToken.get());

                int cookieMaxAge = (int) refreshTokenExpiry / 60;
            /*
            현재 들어있는 리프레쉬 토큰 날려버리고, 새로운 리프레쉬 토큰 쿠키에 넣어준다!
             */
                CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
                CookieUtil.addCookie(response, REFRESH_TOKEN, authRefreshToken.getToken(), cookieMaxAge);
            }
            // 반환하기
            return ResponseEntity.ok(new TokenSucessDto(200,true,"token", newAccessToken.getToken()));
        }

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