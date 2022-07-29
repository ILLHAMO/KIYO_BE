package project.kiyobackend.user.adapter.presentation.dto;

import project.kiyobackend.user.application.dto.*;

import java.util.List;
import java.util.stream.Collectors;

public class UserAssembler {

    public static List<UserReviewResponse> userReviewResponses(List<UserReviewResponseDto> userReviewResponseDtos)
    {
        return userReviewResponseDtos.stream()
                .map(u ->
                        new UserReviewResponse(u.getReviewId(),
                                u.getStoreName(),
                                u.getAddress(),
                                u.getScore(),
                                u.getContent()))
                .collect(Collectors.toList());
    }

    public static UserSignupNicknameRequestDto userSignupNicknameRequestDto(UserSignupNicknameRequest userSignupNicknameRequest)
    {
        return new UserSignupNicknameRequestDto(userSignupNicknameRequest.getNickname());
    }

    public static UserSignupNicknameResponse userSignupNicknameResponse(UserSignupNicknameResponseDto userSignupNicknameResponseDto)
    {
        return new UserSignupNicknameResponse(userSignupNicknameResponseDto.getNickname());
    }

    public static UserProfileResponse userProfileResponse(UserProfileResponseDto userProfileResponseDto)
    {
        return new UserProfileResponse(userProfileResponseDto.getProfileImagePath(),userProfileResponseDto.getNickname());
    }

    public static ChangeUserProfileResponse changeUserProfileResponse(ChangeUserProfileResponseDto changeUserProfileResponseDto)
    {
        return new ChangeUserProfileResponse(changeUserProfileResponseDto.getNickname(), changeUserProfileResponseDto.getProfileImageUrl());
    }
}
