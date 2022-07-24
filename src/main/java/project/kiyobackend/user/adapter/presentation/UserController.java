package project.kiyobackend.user.adapter.presentation;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.kiyobackend.auth.entity.CurrentUser;
import project.kiyobackend.store.application.StoreService;
import project.kiyobackend.user.adapter.presentation.dto.*;
import project.kiyobackend.user.application.UserService;
import project.kiyobackend.user.application.dto.UserProfileResponseDto;
import project.kiyobackend.user.application.dto.UserReviewResponseDto;
import project.kiyobackend.user.application.dto.UserSignupNicknameResponseDto;
import project.kiyobackend.user.domain.User;

import java.util.List;

@RestController
@Tag(name = "USER API",description = "사용자 관련 API")
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final StoreService storeService;

    @Operation(summary = "로그인 유저가 작성한 리뷰 조회")
    @GetMapping("/review")
    public ResponseEntity<List<UserReviewResponse>> getCurrentUserReview(@CurrentUser User user)
    {
        List<UserReviewResponseDto> userReview = userService.getUserReview(user.getUserId());
        return ResponseEntity.ok(UserAssembler.userReviewResponses(userReview));
    }


    @Operation(summary = "로그인 유저의 프로필 정보 조회")
    @ApiResponse(responseCode = "200",description = "OK")
    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(@CurrentUser User user)
    {
        UserProfileResponseDto userProfile = userService.getUserProfile(user);
        return ResponseEntity.ok(UserAssembler.userProfileResponse(userProfile));
    }

    @Operation(summary = "유저가 처음 가입할때 닉네임이 없으면 설정하는 페이지로 이동")
    @PutMapping("/nickname")
    public ResponseEntity<UserSignupNicknameResponse> addNicknameForSignup(@RequestBody UserSignupNicknameRequest userSignupNicknameRequest,@CurrentUser User user)
    {
        UserSignupNicknameResponseDto userSignupNicknameResponseDto = userService.addNicknameForSignup(user, UserAssembler.userSignupNicknameRequestDto(userSignupNicknameRequest));
        return ResponseEntity.ok(UserAssembler.userSignupNicknameResponse(userSignupNicknameResponseDto));
    }

    @Operation(summary = "유저가 등록한 가게 목록 조회")
    @GetMapping("/store")
    public void getUserAssignedStore(@CurrentUser User user)
    {
        User findUser = userService.getUser(user.getUserId());
        storeService.getStoreCurrentUserAssigned(findUser.getAssignedStoreList());
    }







}
