package project.kiyobackend.common;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import project.kiyobackend.auth.entity.RoleType;
import project.kiyobackend.auth.entity.SnsType;
import project.kiyobackend.auth.entity.UserPrincipal;
import project.kiyobackend.auth.service.CustomUserDetailsService;
import project.kiyobackend.user.domain.User;
import project.kiyobackend.user.domain.UserRepository;

import java.lang.annotation.Annotation;
import java.util.List;

@RequiredArgsConstructor
public class WithAuthUserSecurityContextFactory implements WithSecurityContextFactory<WithAuthUser> {

    private final UserRepository userRepository;
    private final CustomUserDetailsService userDetailsService;

    @Override
    public SecurityContext createSecurityContext(WithAuthUser annotation) {

        String userId = annotation.userId();
        // 테스트용 사용자 생성
        // 테스트용 사용자를 저장소에서 가져옴

//        userRepository.save(new User("jemin",
//                "SeoJemin", "jemin3161@naver.com",
//                "Y",
//                "profile",
//                SnsType.GOOGLE,
//                RoleType.USER));
//        // 내 로직에서는 실제 사용자를 가져오기 때문에 loadUserByUsername 호출
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        System.out.println(userDetails.getUsername());
        // Authentication 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, "password", userDetails.getAuthorities());
        // SecurityContextHolder에 주입
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authenticationToken);
        return context;

    }
}
