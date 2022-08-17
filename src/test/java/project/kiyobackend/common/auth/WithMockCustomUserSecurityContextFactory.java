package project.kiyobackend.common.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import project.kiyobackend.auth.entity.RoleType;
import project.kiyobackend.auth.entity.UserPrincipal;
import project.kiyobackend.common.factory.UserFactory;

import java.util.Collections;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UserPrincipal userPrincipal = new UserPrincipal(UserFactory.createUser( customUser.userId()), Collections.singletonList(new SimpleGrantedAuthority(RoleType.USER.getCode())));
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userPrincipal, userPrincipal.getPassword(), userPrincipal.getAuthorities());
        context.setAuthentication(auth);
        return context;

    }
}
