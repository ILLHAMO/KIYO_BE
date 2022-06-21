package project.kiyobackend.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project.kiyobackend.auth.token.AuthTokenProvider;

@Configuration
public class JwtConfig {

    // application.yml로 부터 가져옴
    @Value("${jwt.secret}")
    private String secret;

    // AuthTokenProvider를 스프링 컨테이너에 등록
    @Bean
    public AuthTokenProvider jwtProvider()
    {
        return new AuthTokenProvider(secret);
    }
}
