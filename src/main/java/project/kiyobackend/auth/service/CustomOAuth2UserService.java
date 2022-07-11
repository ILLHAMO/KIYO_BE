package project.kiyobackend.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import project.kiyobackend.auth.entity.SnsType;
import project.kiyobackend.auth.entity.RoleType;
import project.kiyobackend.auth.entity.UserPrincipal;
import project.kiyobackend.auth.exception.OAuthProviderMissMatchException;
import project.kiyobackend.auth.info.OAuth2UserInfo;
import project.kiyobackend.auth.info.OAuth2UserInfoFactory;
import project.kiyobackend.exception.user.NotExistUserException;
import project.kiyobackend.user.domain.User;
import project.kiyobackend.user.domain.UserRepository;


@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;


    /**
     * userRequest 내부에는 access Token, 리다이렉트 uri 등등 각종 정보들이 들어있음
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        /*
        userRequest 내부에는 accessToken에 대한 정보 존재
        이 토큰을 사용해서 super.loadUser 내부에서 사용자에 대한 정보를 받아옴
        User entity의 userId 값은 OAuth2User 내부의 attributes의 id 값을 사용한다.
         */
        OAuth2User user = super.loadUser(userRequest);
        try {
            return this.process(userRequest, user);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {
        SnsType snsType = SnsType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        /*
        이 부분에서 providerType에 따라서 다른 userInfo를 가져온다.
        KAKAO 로그인을 했다면 OAuth2UserInfo에는 KakaoOAuth2UserInfo가 들어옴
         */
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(snsType, user.getAttributes());
        User savedUser = userRepository.findByUserId(userInfo.getId()).orElseThrow(NotExistUserException::new);

        if (savedUser != null) {
            if (snsType != savedUser.getSnsType()) {
                throw new OAuthProviderMissMatchException(
                        "Looks like you're signed up with " + snsType +
                                " account. Please use your " + savedUser.getSnsType() + " account to login."
                );
            }
            updateUser(savedUser, userInfo);
        } else {
            savedUser = createUser(userInfo, snsType);
        }

        return UserPrincipal.create(savedUser, user.getAttributes());
    }

    // 리소스 서버로부터 유저 정보 가져옴 이때 DB에 해당 ID를 가진 사람이 없다면 createUser 획득
    private User createUser(OAuth2UserInfo userInfo, SnsType snsType) {
        User user = new User(
                userInfo.getId(),
                userInfo.getName(),
                userInfo.getEmail(),
                "Y",
                userInfo.getImageUrl(),
                snsType,
                RoleType.USER

        );

        return userRepository.saveAndFlush(user);
    }

    // 만약 있다면 updateUser 획득!
    private User updateUser(User user, OAuth2UserInfo userInfo) {
        if (userInfo.getName() != null && !user.getUsername().equals(userInfo.getName())) {
            user.setUsername(userInfo.getName());
        }

        if (userInfo.getImageUrl() != null && !user.getProfileImageUrl().equals(userInfo.getImageUrl())) {
            user.setProfileImageUrl(userInfo.getImageUrl());
        }

        return user;
    }
}
