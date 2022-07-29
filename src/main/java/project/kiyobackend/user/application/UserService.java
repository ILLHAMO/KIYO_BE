package project.kiyobackend.user.application;

import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import project.kiyobackend.exception.user.NotExistUserException;
import project.kiyobackend.review.domain.domain.Review;
import project.kiyobackend.store.adapter.infrastructure.AWSS3UploadService;
import project.kiyobackend.user.application.dto.*;
import project.kiyobackend.user.domain.User;
import project.kiyobackend.user.domain.UserRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AWSS3UploadService uploadService;

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
    public ChangeUserProfileResponseDto changeUserProfile(User currentUser, String nickname, MultipartFile profileImage)
    {
        // transaction 내에 있으므로 영속 상태로 관리, dirty checking 먹힘
        User user = userRepository.findByUserId(currentUser.getUserId()).orElseThrow(NotExistUserException::new);
        String profileImageUrl = getMultipartFileNames(List.of(profileImage)).get(0);
        user.changeUserProfile(nickname,profileImageUrl);
        return new ChangeUserProfileResponseDto(user.getNickname(),user.getProfileImageUrl());
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

    private List<String> getMultipartFileNames(List<MultipartFile> multipartFiles) {
        List<String> fileNameList = new ArrayList<>();

        multipartFiles.forEach(file->{
            String fileName = createFileName(file.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try(InputStream inputStream = file.getInputStream()) {
                uploadService.uploadFile(inputStream,objectMetadata,fileName);
            } catch(IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
            }
            fileNameList.add(uploadService.getFileUrl(fileName));
        });
        return fileNameList;
    }

    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // file 형식이 잘못된 경우를 확인하기 위해 만들어진 로직이며, 파일 타입과 상관없이 업로드할 수 있게 하기 위해 .의 존재 유무만 판단하였습니다.
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("잘못된 형식 입니다.");
        }
    }

}
