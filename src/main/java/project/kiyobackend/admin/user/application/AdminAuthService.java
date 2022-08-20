package project.kiyobackend.admin.user.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.kiyobackend.admin.exception.AdminLoginException;
import project.kiyobackend.admin.user.application.dto.AdminResponseDto;
import project.kiyobackend.auth.config.properties.AppProperties;
import project.kiyobackend.auth.repository.UserRefreshTokenRepository;
import project.kiyobackend.auth.token.AuthToken;
import project.kiyobackend.auth.token.AuthTokenProvider;
import project.kiyobackend.auth.token.UserRefreshToken;
import project.kiyobackend.exception.user.NotExistUserException;
import project.kiyobackend.user.domain.User;
import project.kiyobackend.user.domain.UserRepository;

import java.util.Date;


@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthTokenProvider tokenProvider;
    private final AppProperties appProperties;
    private final UserRefreshTokenRepository userRefreshTokenRepository;

    @Transactional
    public AdminResponseDto signin(String adminId, String password)
    {
        User findUser = userRepository.findByUserId(adminId).orElseThrow(()->new AdminLoginException("관리자 아이디가 틀렸습니다."));
        System.out.println(findUser.getRoleType().getCode());
        System.out.println(findUser.getPassword());
        System.out.println(password);
        boolean check = passwordEncoder.matches(password,findUser.getPassword());
        if(check)
        {
            Date now = new Date();

            // 인증 성공 후 엑세스 토큰 만들어내는 로직
            AuthToken accessToken = tokenProvider.createAuthToken(
                    findUser.getUserId(),
                    findUser.getRoleType().getCode(),
                    new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
            );

            // refresh 토큰 설정
            long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

            // refreshToken은 최소한의 정보만 집어넣음
            AuthToken refreshToken = tokenProvider.createAuthToken(
                    appProperties.getAuth().getTokenSecret(),
                    new Date(now.getTime() + refreshTokenExpiry)
            );

            //1. DB에서 ID로 refreshToken 찾는다.
            UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserId(findUser.getUserId());
            //2. 기존 refreshToken 있으면 refreshtoken 값 업데이트
            if (userRefreshToken != null) {
                userRefreshToken.setRefreshToken(refreshToken.getToken());
                userRefreshTokenRepository.saveAndFlush(userRefreshToken);
            } else {
                //3. 기존에 없으면 새로 만들어서 저장
                userRefreshToken = new UserRefreshToken(findUser.getUserId(), refreshToken.getToken());
                userRefreshTokenRepository.saveAndFlush(userRefreshToken);
            }

            return new AdminResponseDto(findUser.getUserSeq(),accessToken.getToken(),refreshToken.getToken());

        }
        else {
            throw new AdminLoginException("관리자 비밀번호가 틀렸습니다.");
        }

    }
}
