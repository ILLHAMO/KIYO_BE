package project.kiyobackend.user.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import project.kiyobackend.auth.entity.UserPrincipal;
import project.kiyobackend.common.factory.UserFactory;
import project.kiyobackend.user.application.dto.UserProfileResponseDto;
import project.kiyobackend.user.domain.User;
import project.kiyobackend.user.domain.UserRepository;

import java.util.Optional;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @DisplayName("유저 ID로 USER 조회")
    @Test
    void getUser()
    {
        //given
        User loginUser = UserFactory.createUser(1L, "jemin");
        String userId = loginUser.getUserId();
        given(userRepository.findByUserId(anyString())).willReturn(Optional.of(loginUser));

        //when
        User findUser = userService.getUser("jemin");

        //then
        BDDMockito.verify(userRepository,times(1)).findByUserId(userId);
        Assertions.assertThat(findUser.getUserSeq()).isEqualTo(1L);
    }

    @DisplayName("로그인된 사용자의 프로필 정보 조회")
 //   @Test
    @WithMockUser
    void getUserProfile()
    {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserProfileResponseDto result = userService.getUserProfile(principal.getUser());
        Assertions.assertThat(result.getProfileImagePath()).isEqualTo("s3://ap-north-east");
        Assertions.assertThat(result.getNickname()).isEqualTo("jemini");
    }


}