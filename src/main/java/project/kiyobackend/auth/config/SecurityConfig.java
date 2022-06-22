package project.kiyobackend.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import project.kiyobackend.auth.config.properties.AppProperties;
import project.kiyobackend.auth.config.properties.CorsProperties;
import project.kiyobackend.auth.entity.RoleType;
import project.kiyobackend.auth.exception.RestAuthenticationEntryPoint;
import project.kiyobackend.auth.filter.TokenAuthenticationFilter;
import project.kiyobackend.auth.handler.OAuth2AuthenticationFailureHandler;
import project.kiyobackend.auth.handler.OAuth2AuthenticationSuccessHandler;
import project.kiyobackend.auth.handler.TokenAccessDeniedHandler;
import project.kiyobackend.auth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import project.kiyobackend.auth.repository.UserRefreshTokenRepository;
import project.kiyobackend.auth.service.CustomOAuth2UserService;
import project.kiyobackend.auth.service.CustomUserDetailsService;
import project.kiyobackend.auth.token.AuthTokenProvider;

import java.util.Arrays;



@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsProperties corsProperties;
    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;
    private final CustomOAuth2UserService oAuth2UserService;
    private final TokenAccessDeniedHandler tokenAccessDeniedHandler;
    private final UserRefreshTokenRepository userRefreshTokenRepository;

    /*
     * UserDetailsService 설정
     * */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // cors 정책 적용
                .cors()
                .and()
                // JWT를 사용함으로 세션 사용 안함
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // JWT 사용함으로 아래의 쿠키 세션 로그인 방식 연관 속성 다 disable
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                // 예외 발생할때 exception 처리하는 객체들 등록
                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .accessDeniedHandler(tokenAccessDeniedHandler)

                .and()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                // api로 시작하는 요소들은 USER 이상의 권한 있으면 다 사용 가능
                .antMatchers("/api/**").hasAnyAuthority(RoleType.USER.getCode())
                // admin 경로는 admin만 사용가능
                .antMatchers("/api/**/admin/**").hasAnyAuthority(RoleType.ADMIN.getCode())
                //.anyRequest().authenticated()
                .anyRequest().permitAll()
                .and()
                // oauth2 로그인 활성화
                .oauth2Login()
                .authorizationEndpoint()
                // 기본 인증 경로 /oauth2/authorization
                .baseUri("/oauth2/authorization")
                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
                .and()
                // 리다이렉트 될때
                .redirectionEndpoint()
                .baseUri("/*/oauth2/code/*")
                .and()
                .userInfoEndpoint()
                .userService(oAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler())
                .failureHandler(oAuth2AuthenticationFailureHandler());

        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    /*
     * auth 매니저 설정
     * */
    @Override
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    /*
     * security 설정 시, 사용할 인코더 설정
     * */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * 토큰 필터 설정
     * */
    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }

    /*
     * 쿠키 기반 인가 Repository
     * 인가 응답을 연계 하고 검증할 때 사용.
     * */
    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    /*
     * Oauth 인증 성공 핸들러
     * */
    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(
                tokenProvider,
                appProperties,
                userRefreshTokenRepository,
                oAuth2AuthorizationRequestBasedOnCookieRepository()
        );
    }

    /*
     * Oauth 인증 실패 핸들러
     * */
    @Bean
    public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler(oAuth2AuthorizationRequestBasedOnCookieRepository());
    }

    /*
     * Cors 설정
     * */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedHeaders(Arrays.asList(corsProperties.getAllowedHeaders().split(",")));
        corsConfig.setAllowedMethods(Arrays.asList(corsProperties.getAllowedMethods().split(",")));
        corsConfig.setAllowedOrigins(Arrays.asList(corsProperties.getAllowedOrigins().split(",")));
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(corsConfig.getMaxAge());

        corsConfigSource.registerCorsConfiguration("/**", corsConfig);
        return corsConfigSource;
    }
}