package project.kiyobackend.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project.kiyobackend.auth.service.CustomUserDetailsService;
import project.kiyobackend.auth.token.AuthTokenProvider;

@Configuration
public class JwtConfig {


    private String secret;

    @Bean
    public AuthTokenProvider jwtProvider(CustomUserDetailsService customUserDetailsService, @Value("${jwt.secret}") String secret)
    {   this.secret = secret;
        return new AuthTokenProvider(customUserDetailsService,secret);
    }
}
