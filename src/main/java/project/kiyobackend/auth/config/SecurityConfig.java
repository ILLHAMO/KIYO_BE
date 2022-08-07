package project.kiyobackend.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
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
import project.kiyobackend.auth.filter.JwtExceptionFilter;
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
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsProperties corsProperties;
    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;
    private final CustomOAuth2UserService oAuth2UserService;
    private final TokenAccessDeniedHandler tokenAccessDeniedHandler;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final JwtExceptionFilter jwtExceptionFilter;
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
                /**
                 * cors 정책 적용
                 */
                .cors()
                .and()
                // JWT를 사용함으로 세션 사용 안함
                /**
                 * Session 미적용
                 */
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // JWT 사용함으로 아래의 쿠키 세션 로그인 방식 연관 속성 다 disable
                /**
                 * csrf 등 jwt 로그인 방식에는 필요없는것들 다 비활성화
                 */
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                /**
                 * 예외 처리 구간
                 */
                // 예외 발생할때 exception 처리하는 객체들 등록
                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .accessDeniedHandler(tokenAccessDeniedHandler)

                .and()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                /**
                 * 보완 관련 부분은 다 지움
                 */
                .antMatchers("/auth/**",
                        "/oauth2/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/api-docs",
                        "api/store/search",
                        "/api/search/keyword/rank",
                        "/api/search/keyword/recent"
                )
                .permitAll()
                /**
                 * api 경로는 일반 사용자 접근 가능
                 */
                .antMatchers(HttpMethod.GET,"/api/stores","/api/store/**").permitAll()
                .antMatchers(HttpMethod.GET,"/api/category","/api/convenience").permitAll()
                // 나머지는 USER 권한이 있는 사용자만 가능
                .antMatchers("/api/**").hasAnyAuthority(RoleType.USER.getCode(),RoleType.ADMIN.getCode())
                .antMatchers("/api/admin/**").hasAnyAuthority(RoleType.ADMIN.getCode())
                .anyRequest().authenticated()
                .and()
                /**
                 * 여기부터는 oauth2 로그인 활성화 부분 - 딱히 건들거 없음
                 */
                .oauth2Login()
                .authorizationEndpoint()
                // 기본 인가 경로 /oauth2/authorization
                .baseUri("/authorization/**")
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
        http.addFilterBefore(jwtExceptionFilter,tokenAuthenticationFilter().getClass());
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