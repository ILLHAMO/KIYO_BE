package project.kiyobackend.user.adapter.presentation.dto;

import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.user.application.dto.*;

import java.util.Comparator;
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
                                u.getContent(),
                                u.getUpdatedDate())).sorted(Comparator.comparing(UserReviewResponse::getUpdateTime).reversed())
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

    public static List<StoreCurrentUserAssignedResponseDto> storeCurrentUserAssignedResponseDtoList(List<Store> stores)
    {
        return stores.stream().map(s -> new StoreCurrentUserAssignedResponseDto(s.getId(),s.getName(),s.getAddress(),s.isAssigned()))
                .collect(Collectors.toList());
    }

}
