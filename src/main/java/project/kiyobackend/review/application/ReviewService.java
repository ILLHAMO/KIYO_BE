package project.kiyobackend.review.application;

import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import project.kiyobackend.exception.review.NotExistReviewException;
import project.kiyobackend.exception.store.NotExistStoreException;
import project.kiyobackend.review.adapter.presentation.dto.ReviewAssembler;
import project.kiyobackend.review.application.dto.ReviewRequestDto;
import project.kiyobackend.review.application.dto.ReviewRequestForUpdateDto;
import project.kiyobackend.review.application.dto.ReviewResponseForUpdateDto;
import project.kiyobackend.review.domain.domain.Review;
import project.kiyobackend.review.domain.domain.ReviewRepository;
import project.kiyobackend.store.adapter.infrastructure.AWSS3UploadService;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.store.domain.domain.store.StoreRepository;
import project.kiyobackend.user.application.UserService;
import project.kiyobackend.user.domain.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final StoreRepository storeRepository;
    private final AWSS3UploadService uploadService;

    public ReviewResponseForUpdateDto getReviewForUpdateForm(Long reviewId)
    {
        Review review = reviewRepository.findById(reviewId).orElseThrow(NotExistReviewException::new);
        return ReviewAssembler.ReviewResponseForUpdateDto(review);
    }

    @Transactional
    public Long saveReview(String userId,Long storeId, List<MultipartFile> multipartFiles, ReviewRequestDto reviewRequestDto)
    {
        User user = userService.getUser(userId);
        Store store = storeRepository.findById(storeId).orElseThrow(NotExistStoreException::new);
        List<String> fileNameList = getMultipartFileNames(multipartFiles);
        System.out.println(fileNameList);
        Review review = Review.createReview(user, store, reviewRequestDto.getScore(), reviewRequestDto.getContent(), fileNameList);
        Review result = reviewRepository.save(review);
        return result.getId();
    }

    @Transactional
    public void deleteReview(Long reviewId)
    {
        Review review = reviewRepository.findById(reviewId).orElseThrow(NotExistReviewException::new);
        reviewRepository.delete(review);
    }




    @Transactional
    public void updateReview(Long reviewId,List<MultipartFile> multipartFiles, ReviewRequestForUpdateDto reviewRequestDto)
    {
        Review review = reviewRepository.findById(reviewId).orElseThrow(NotExistReviewException::new);

            List<String> fileNameList = getMultipartFileNames(multipartFiles);


        if(!reviewRequestDto.getDeleteIds().isEmpty())
        {
            for(Long deleteId : reviewRequestDto.getDeleteIds())
            {
                review.getReviewImages().removeIf(ri->ri.getId().equals(deleteId));
            }
        }
        review.updateReview(fileNameList,reviewRequestDto.getScore(),reviewRequestDto.getContent());
    }

    private List<String> getMultipartFileNames(List<MultipartFile> multipartFiles) {

        if(!multipartFiles.get(0).getOriginalFilename().isEmpty())
        {
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
        return null;

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
