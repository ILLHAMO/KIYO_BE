package project.kiyobackend.auth.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import project.kiyobackend.user.domain.User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;


// TODO : OidcUser 공부!
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPrincipal implements UserDetails, OAuth2User, OidcUser {

    private User user;
    private  String userId;
    private  String password;
    private  SnsType snsType;
    private  RoleType roleType;
    private  Collection<GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public UserPrincipal(User user, String userId, String password, SnsType snsType, RoleType roleType, Collection<GrantedAuthority> authorities) {
        this.user = user;
        this.userId = userId;
        this.password = password;
        this.snsType = snsType;
        this.roleType = roleType;
        this.authorities = authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return user.getUserId();
    }

//    @Override
//    public String getName() {
//        return user.getUserId();
//    }
//
//    @Override
//    public String getUsername() {
//        return user.getUserId();
//    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getClaims() {
        return null;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return null;
    }

    public static UserPrincipal create(User user) {
        return new UserPrincipal(
                user,
                user.getUserId(),
                user.getPassword(),
                user.getSnsType(),
                RoleType.USER,
                Collections.singletonList(new SimpleGrantedAuthority(RoleType.USER.getCode()))
        );
    }
    public static UserPrincipal create(User user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = create(user);
        userPrincipal.setAttributes(attributes);

        return userPrincipal;
    }


    @Override
    public String getName() {
        return user.getUserId();
    }
}
