package project.kiyobackend.user.adapter.presentation;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import project.kiyobackend.auth.common.UserDto;
import project.kiyobackend.auth.entity.CurrentUser;
import project.kiyobackend.user.adapter.presentation.dto.*;
import project.kiyobackend.user.application.UserService;
import project.kiyobackend.user.application.dto.UserProfileResponseDto;
import project.kiyobackend.user.application.dto.UserReviewResponseDto;
import project.kiyobackend.user.application.dto.UserSignupNicknameResponseDto;
import project.kiyobackend.user.domain.User;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "로그인 유저가 작성한 리뷰 조회")
    @GetMapping("/review")
    public ResponseEntity<List<UserReviewResponse>> getCurrentUserReview(@CurrentUser User user)
    {
        List<UserReviewResponseDto> userReview = userService.getUserReview(user.getUserId());
        return ResponseEntity.ok(UserAssembler.userReviewResponses(userReview));
    }

    @Operation(summary = "로그인 유저의 프로필 정보 조회")
    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(@CurrentUser User user)
    {
        UserProfileResponseDto userProfile = userService.getUserProfile(user);
        return ResponseEntity.ok(UserAssembler.userProfileResponse(userProfile));
    }

    @Operation(summary = "유저가 처음 가입할때 닉네임이 없으면 설정하는 페이지로 이동")
    @PutMapping("/nickname")
    public ResponseEntity<UserSignupNicknameResponse> addNicknameForSignup(@CurrentUser User user, @RequestBody UserSignupNicknameRequest userSignupNicknameRequest)
    {
        UserSignupNicknameResponseDto userSignupNicknameResponseDto = userService.addNicknameForSignup(user, UserAssembler.userSignupNicknameRequestDto(userSignupNicknameRequest));
        return ResponseEntity.ok(UserAssembler.userSignupNicknameResponse(userSignupNicknameResponseDto));
    }




}
