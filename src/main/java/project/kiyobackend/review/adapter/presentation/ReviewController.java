package project.kiyobackend.review.adapter.presentation;

import com.amazonaws.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.kiyobackend.auth.entity.CurrentUser;
import project.kiyobackend.common.SuccessResponseDto;
import project.kiyobackend.review.adapter.presentation.dto.ReviewAssembler;
import project.kiyobackend.review.adapter.presentation.dto.ReviewRequest;
import project.kiyobackend.review.adapter.presentation.dto.ReviewResponse;
import project.kiyobackend.review.adapter.presentation.dto.ReviewResponseForUpdate;
import project.kiyobackend.review.application.ReviewService;
import project.kiyobackend.review.application.dto.ReviewResponseForUpdateDto;
import project.kiyobackend.store.adapter.presentation.dto.store.StoreRequestDto;
import project.kiyobackend.user.domain.User;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 등록",description = "score는 'HIGH','MIDDLE','LOW' 세가지 타입으로 구성되어있다.")
    @PostMapping(value = "/review/store/{storeId}")
    public ResponseEntity<ReviewResponse> saveReview(
            @RequestPart(name = "meta_data") ReviewRequest reviewRequest,
            @RequestPart(name = "multipartFiles") List<MultipartFile> multipartFiles,
            @CurrentUser User user,
            @PathVariable Long storeId)
    {

        Long result = reviewService.saveReview(user.getUserId(), storeId, multipartFiles, ReviewAssembler.reviewRequestDto(reviewRequest));
        return ResponseEntity.ok(new ReviewResponse(true,result));
    }

    @Operation(summary = "리뷰 수정")
    @PutMapping("/review/{reviewId}")
    public ResponseEntity<SuccessResponseDto> updateReview(@PathVariable Long reviewId,
                                                           @RequestPart(name = "meta_data") ReviewRequest reviewRequest,
                                                           @RequestPart(name = "multipartFiles") List<MultipartFile> multipartFiles)
    {
               reviewService.updateReview(reviewId, multipartFiles,ReviewAssembler.reviewRequestDto(reviewRequest));
               return ResponseEntity.ok(new SuccessResponseDto(true,reviewId));
    }

    @Operation(summary = "리뷰 수정 페이지 데이터")
    @GetMapping("/review/{reviewId}/update")
    public ResponseEntity<ReviewResponseForUpdate> updatePageReview(@PathVariable Long reviewId)
    {
        ReviewResponseForUpdateDto reviewForUpdateForm = reviewService.getReviewForUpdateForm(reviewId);
        return ResponseEntity.ok(ReviewAssembler.ReviewResponseForUpdate(reviewForUpdateForm));
    }

    @Operation(summary = "리뷰 삭제")
    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId)
    {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok("delete success");
    }


}
