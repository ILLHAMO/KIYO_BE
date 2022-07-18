package project.kiyobackend.user.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.kiyobackend.exception.user.NotExistUserException;
import project.kiyobackend.review.domain.domain.Review;
import project.kiyobackend.user.application.dto.UserProfileResponseDto;
import project.kiyobackend.user.application.dto.UserReviewResponseDto;
import project.kiyobackend.user.application.dto.UserSignupNicknameRequestDto;
import project.kiyobackend.user.application.dto.UserSignupNicknameResponseDto;
import project.kiyobackend.user.domain.User;
import project.kiyobackend.user.domain.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUser(String userId) {
        return userRepository.findByUserId(userId).orElseThrow(NotExistUserException::new);
    }

    // 해당 사용자의 리뷰 다 가져오기
    public List<UserReviewResponseDto> getUserReview(String userId)
    {
        // 1. User를 찾고
        User user = userRepository.findByUserId(userId).orElseThrow(NotExistUserException::new);
        List<Review> reviews = user.getReviews();

        return reviews.stream().map(r ->
                new UserReviewResponseDto(r.getId(), r.getStore().getName(), r.getStore().getAddress(), r.getScore(), r.getContent()))
                .collect(Collectors.toList());
    }

    @Transactional
    public UserSignupNicknameResponseDto addNicknameForSignup(User currentUser, UserSignupNicknameRequestDto userSignupNicknameRequestDto)
    {
        User user = userRepository.findByUserId(currentUser.getUserId()).orElseThrow(NotExistUserException::new);
        user.setNickname(userSignupNicknameRequestDto.getNickname());
        return new UserSignupNicknameResponseDto(user.getNickname());
    }

    public UserProfileResponseDto getUserProfile(User currentUser)
    {
        return new UserProfileResponseDto(currentUser.getProfileImageUrl(), currentUser.getNickname());
    }



}
